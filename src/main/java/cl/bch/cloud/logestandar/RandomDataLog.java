//package cl.bch.cloud.logestandar;
//
//import java.util.logging.Logger;
//
//import ch.qos.logback.core.joran.action.Action;
//import cl.bch.cloud.dtos.MessageDTO;
//import lombok.NoArgsConstructor;
//
//import java.util.Map;
//import java.util.Objects;
//
///**
// * Clase para generar Log Estándar utilizando java.util.logging.
// * Se pueden implementar métodos de DataLogEstandar para setear datos de Data Fija y se DEBE implementar
// * generarDataVariable para poder generar la Data Variable del log específico.
// *
// * La clase LogEstándarUtils provee métodos para recuperar del mapa de data la información de:
// * - argumentos del controller
// * - excepción lanzada (si es que existe)
// * - objeto de respuesta
// */
//@NoArgsConstructor
//public class RandomDataLog implements DataLogEstandar {
//
//    private static final Logger logger = Logger.getLogger(RandomDataLog.class.getName());
//
//    private final static String NOK = "NOK";
//
//    @Override
//    public String resultado(Map<Action, Object> data) {
//        var response = LogEstandarUtils.getResponse(data, MessageDTO.class);
//        String result;
//        if (Objects.nonNull(response) && NOK.equals(response.status())) {
//            result = "R";
//        } else {
//            result = "A";
//        }
//        logger.info("Resultado generado: " + result);
//        return result;
//    }
//
//    @Override
//    public String generarDataVariable(Map<Action, Object> data) {
//        var maxRandom = LogEstandarUtils.getArgument(data, Integer.class);
//        var response = LogEstandarUtils.getResponse(data, MessageDTO.class);
//        var strbuilder = new StringBuilder();
//
//        strbuilder.append(LogUtils.formatMensaje(String.valueOf(maxRandom), 6, Relleno.ZERO, Alineacion.DERECHA))
//                .append(LogUtils.formatMensaje(response.message(), 150, Relleno.ESPACIO, Alineacion.IZQUIERDA));
//
//        String dataVariable = strbuilder.toString();
//        logger.fine("Data variable generada: " + dataVariable);
//
//        return dataVariable;
//    }
//}
