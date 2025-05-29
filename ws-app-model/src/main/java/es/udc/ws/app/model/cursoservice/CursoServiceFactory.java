package es.udc.ws.app.model.cursoservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class CursoServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "CursoServiceFactory.className";
    private static CursoService service = null;

    private CursoServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static CursoService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (CursoService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static CursoService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }

}
