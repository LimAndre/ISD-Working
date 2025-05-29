package es.udc.ws.app.model.inscripcion;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlInscripcionDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlInscripcionDaoFactory.className";
    private static SqlInscripcionDao dao = null;

    private SqlInscripcionDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlInscripcionDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlInscripcionDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlInscripcionDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }

}
