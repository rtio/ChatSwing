/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jupitertec.chat.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael S. Meneses
 */
public class Log {

    public static void gravar(String log) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
        String formatedDate = sdf.format(new Date());
        FileWriter arq = null;
        try {
            arq = new FileWriter("./properties/log.txt", true);
            PrintWriter gravarArq = new PrintWriter(arq);
            gravarArq.println(formatedDate + " | " + log + "\n");
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                arq.close();
            } catch (IOException ex) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
