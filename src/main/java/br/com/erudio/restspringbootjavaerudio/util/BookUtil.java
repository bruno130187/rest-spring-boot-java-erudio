package br.com.erudio.restspringbootjavaerudio.util;

import java.math.BigDecimal;

public class BookUtil {

    public static String formataValorParaRealString(BigDecimal valorBigdecimal) {
        String retorno =  "R$ " + valorBigdecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return retorno.replace(".", ",");
    }

}
