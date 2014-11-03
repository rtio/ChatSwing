/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jupitertec.chat.util;

/**
 *
 * @author Rafael S. Meneses
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Manipulador {

    public static String getProp(String propertie) throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream("./properties/config.properties");
        props.load(file);
        return props.getProperty(propertie);
    }

}