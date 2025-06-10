package es.udc.ws.app.thriftservice;

import es.udc.ws.util.servlet.ThriftHttpServletTemplate;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;

import es.udc.ws.app.cursos.thrift.ThriftCursoService;

public class ThriftCursoServiceServlet extends ThriftHttpServletTemplate {

    public ThriftCursoServiceServlet() {
        super(createProcessor(), createProtocolFactory());
    }

    private static TProcessor createProcessor() {

        return new ThriftCursoService.Processor<ThriftCursoService.Iface>(new ThriftCursoServiceImpl());

    }

    private static TProtocolFactory createProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }

}
