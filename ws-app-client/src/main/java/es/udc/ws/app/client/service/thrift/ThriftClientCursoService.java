package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientCursoService;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.app.client.service.exceptions.ClientCourseClosedException;
import es.udc.ws.app.client.service.exceptions.ClientCourseFullException;
import es.udc.ws.app.client.service.thrift.ClientCursoDtoToThriftCursoDtoConversor;
import es.udc.ws.app.client.service.thrift.ClientInscripcionDtoToThriftInscripcionDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.cursos.thrift.ThriftCursoService;
import es.udc.ws.cursos.thrift.ThriftCursoDto;
import es.udc.ws.cursos.thrift.ThriftInputValidationException;
import es.udc.ws.cursos.thrift.ThriftInstanceNotFoundException;
import es.udc.ws.cursos.thrift.ThriftCourseClosedException;
import es.udc.ws.cursos.thrift.ThriftCourseFullException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

public class ThriftClientCursoService implements ClientCursoService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientCursoService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);


    @Override
    public Long addCurso(ClientCursoDto curso) throws InputValidationException {

        ThriftCursoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            return client.altaCurso(ClientCursoDtoToThriftCursoDtoConversor.toThriftCursoDto(curso)).getIdCurso();
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientCursoDto> findCursos(String ciudad){

        ThriftCursoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();


            return ClientCursoDtoToThriftCursoDtoConversor.toClientCursoDtos(
                    client.buscarCursosByFechaYCiudad(ciudad));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long inscribirCurso(ClientInscripcionDto inscripcionDto)
            throws InputValidationException,
            InstanceNotFoundException,
            ClientCourseClosedException,
            ClientCourseFullException {

        ThriftCursoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            // Invocación Thrift:
            return client.inscribirUsuario(
                    ClientInscripcionDtoToThriftInscripcionDtoConversor
                            .toThriftInscripcionDto(inscripcionDto)
            );
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftCourseClosedException e) {
            throw new ClientCourseClosedException(e.getCursoId());
        } catch (ThriftCourseFullException e) {
            throw new ClientCourseFullException(e.getCursoId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private ThriftCursoService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftCursoService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }

}
