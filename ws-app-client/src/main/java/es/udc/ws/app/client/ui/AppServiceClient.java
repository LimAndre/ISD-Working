package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientCursoService;
import es.udc.ws.app.client.service.ClientCursoServiceFactory;
import es.udc.ws.app.client.service.dto.ClientCursoDto;
import es.udc.ws.app.client.service.dto.ClientInscripcionDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class AppServiceClient {

    public static void main(String[] args) {
        // TODO

        if (args.length == 0) {
            printUsageAndExit();
        };

        ClientCursoService clientCursoService = ClientCursoServiceFactory.getService();


        if ("-a".equalsIgnoreCase(args[0])) {

            validateArgs(args, 6, new int[] {4, 5});

            // [add] AppServiceClient -a <ciudad> <nombre> <fechaInicio> <precio> <maxPlazas>

            try {

                LocalDateTime fechaInicio = LocalDateTime.parse(args[3]);

                Long courseId = clientCursoService.addCurso(new ClientCursoDto(
                        null,                // courseId (se asigna en servidor)
                        args[1],                    // ciudad
                        args[2],                    // nombre
                        fechaInicio,                // fechaInicio (ISO-8601: yyyy-MM-dd'T'HH:mm)
                        Float.valueOf(args[4]),     // precio
                        Integer.valueOf(args[5])    // maxPlazas
                ));

                System.out.println("Curso " + courseId + " creado");
            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }
        else if ("-f".equalsIgnoreCase(args[0])) {
            // [find]   AppServiceClient -f <ciudad>
            validateArgs(args, 2, new int[] {});
            try {
                List<ClientCursoDto> cursos = clientCursoService.findCursos(args[1]);
                System.out.println("Encontrados " + cursos.size() +
                        " curso(s) en '" + args[1] + "'");
                for (ClientCursoDto courseDto : cursos) {
                    System.out.println("Id: " + courseDto.getCursoId() +
                            ", Nombre: " + courseDto.getNombreCurso() +
                            ", Fecha de inicio: " + courseDto.getFechaInicio() +
                            ", Precio: " + courseDto.getPrecio() +
                            ", Plazas Reservadas: " + courseDto.getPlazasReservadas());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }
        else if ("-i".equalsIgnoreCase(args[0])) {
            // [inscribirse] AppServiceClient -i <courseId> <emailUsuario> <numeroTarjeta>
            validateArgs(args, 4, new int[] {1});
            try {
                Long inscriptionId = clientCursoService.inscribirCurso(
                        Long.parseLong(args[1]),  // courseId
                        args[2],                  // emailUsuario
                        args[3]                   // numeroTarjeta
                );

                System.out.println("Curso: " + args[1] + ". Se ha suscrito satisfactoriamente " + inscriptionId);
            } catch (NumberFormatException | InstanceNotFoundException |
                     InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

          }
        else {
            System.err.println("Opcion no valida: " + args[0]);
            printUsageAndExit();
        }
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if (expectedArgs != args.length) {
            printUsageAndExit();
        }
        for (int position : numericArguments) {
            try {
                Double.parseDouble(args[position]);
            } catch (NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [add]    AppServiceClient -a <ciudad> <nombre> <fechaInicio> <precio> <maxPlazas>\n" +
                "    [find]   AppServiceClient -s <ciudad>\n" +
                "    [inscribirse] AppServiceClient -i <courseId> <emailUsuario> <numeroTarjeta>\n" +
                "    [help]   AppServiceClient -h\n\n" +
                "  <fechaInicio>    Formato ISO-8601: yyyy-MM-dd'T'HH:mm (p.ej., 2025-07-01T10:00)\n" +
                "  <precio>         Número decimal (p.ej., 150.00)\n" +
                "  <maxPlazas>      Número entero (p.ej., 30)\n" +
                "  <courseId>       Identificador numérico del curso\n" +
                "  <emailUsuario>   E-mail del usuario (p.ej., juan@ejemplo.com)\n" +
                "  <numeroTarjeta>  Número de tarjeta (16 dígitos, sin espacios)");
    }

}