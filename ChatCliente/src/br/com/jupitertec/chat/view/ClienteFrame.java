/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jupitertec.chat.view;

import br.com.jupitertec.chat.bean.ChatMessage;
import br.com.jupitertec.chat.bean.ChatMessage.Action;
import br.com.jupitertec.chat.service.ClienteService;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Rafael S. Meneses
 */
public class ClienteFrame extends javax.swing.JFrame {
    
    private Socket socket;
    private ChatMessage message;
    private ClienteService service;
    private JOptionPane input;

    /**
     * Creates new form ClienteFrame
     */
    public ClienteFrame() {
        initComponents();
        begin();
        connect();
    }
    
    private void begin(){
        jTextFieldTextName.setText(input.showInputDialog("Digite seu nome!"));
        jTextAreaSend.setCaretPosition(jTextAreaSend.getDocument().getLength());
    }
    private void connect(){
            String name = jTextFieldTextName.getText();
        if (!name.isEmpty()) {
            //JOptionPane.showMessageDialog(null, name);
            this.message = new ChatMessage();
            this.message.setAction(Action.CONNECT);
            this.message.setName(name);
            
            this.service = new ClienteService();
            this.socket = this.service.connect();
            
            new Thread(new ListenerSocket(this.socket)).start();
            
            this.service.send(message);
        }
    }
    private class ListenerSocket implements Runnable {
        
        private ObjectInputStream input;
        
        public ListenerSocket(Socket socket) {
            try {
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @Override
        public void run() {
            ChatMessage message = null;
            try {
                while ((message = (ChatMessage) input.readObject()) != null) {
                    Action action = message.getAction();
                    
                    if (action.equals(Action.CONNECT)) {
                        connected(message);
                    } else if (action.equals(Action.DISCONNECT)) {
                        disconnected();
                        socket.close();
                    } else if (action.equals(Action.SEND_ONE)) {
                        receive(message);
                    } else if (action.equals(Action.USERS_ONLINE)) {
                        refreshOnline(message);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    private void connected(ChatMessage message) {
        if (message.getText().equals("NO")) {
            this.jTextFieldTextName.setText("");
            JOptionPane.showMessageDialog(this, "Conexão não realizada.\nTente com outro nome!");
            return;
        }
        this.message = message;
        this.jButtonConectar.setEnabled(false);
        this.jTextFieldTextName.setEditable(false);
        
        this.jButtonSair.setEnabled(true);
        this.jTextAreaSend.setEnabled(true);
        this.jTextAreaReceive.setEnabled(true);
        this.jButtonEnviar.setEnabled(true);
        this.jButtonLimpar.setEnabled(true);
        
        JOptionPane.showMessageDialog(this, "Você está conectado!");
    }
    
    private void disconnected() {
        
        this.jButtonConectar.setEnabled(true);
        this.jTextFieldTextName.setEditable(true);
        
        this.jButtonSair.setEnabled(false);
        this.jTextAreaSend.setEnabled(false);
        this.jTextAreaReceive.setEnabled(false);
        this.jButtonEnviar.setEnabled(false);
        this.jButtonLimpar.setEnabled(false);
        
        this.jTextAreaReceive.setText("");
        this.jTextAreaSend.setText("");
        
        JOptionPane.showMessageDialog(this, "Saiu do Chat!");
        
    }
    
    private void receive(ChatMessage message) {
        this.jTextAreaReceive.append(message.getName() + " diz: " + message.getText() + "\n");
    }
    
    private void refreshOnline(ChatMessage message) {
        Set<String> names = message.getSetOnlines();
        
        names.remove((String) message.getName());
        
        String[] array = (String[]) names.toArray(new String[names.size()]);
        
        this.jListOnline.setListData(array);
        this.jListOnline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.jListOnline.setLayoutOrientation(JList.VERTICAL);
    }
    
    private void sendMessage() {
        String text = this.jTextAreaSend.getText();
        String name = this.message.getName();
        
        this.message = new ChatMessage();
        
        if (this.jListOnline.getSelectedIndex() > -1) {
            this.message.setNameReserved((String) this.jListOnline.getSelectedValue());
            this.message.setAction(Action.SEND_ONE);
            this.jListOnline.clearSelection();
        } else {
            this.message.setAction(Action.SEND_ALL);
        }
        
        if (!text.isEmpty()) {
            this.message.setName(name);
            this.message.setText(text);
            
            this.jTextAreaReceive.append("Você disse: " + text + "\n");
            
            this.service.send(this.message);
        }
        this.jTextAreaSend.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextFieldTextName = new javax.swing.JTextField();
        jButtonConectar = new javax.swing.JButton();
        jButtonSair = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListOnline = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaReceive = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaSend = new javax.swing.JTextArea();
        jButtonLimpar = new javax.swing.JButton();
        jButtonEnviar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Conectar"));

        jTextFieldTextName.setBackground(new java.awt.Color(153, 255, 255));

        jButtonConectar.setText("Conectar");
        jButtonConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConectarActionPerformed(evt);
            }
        });

        jButtonSair.setText("Sair");
        jButtonSair.setEnabled(false);
        jButtonSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextFieldTextName, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonConectar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSair)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldTextName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonConectar)
                    .addComponent(jButtonSair))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Online"));

        jListOnline.setBackground(new java.awt.Color(153, 255, 255));
        jListOnline.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListOnline.setToolTipText("");
        jScrollPane3.setViewportView(jListOnline);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Mensagem"));

        jTextAreaReceive.setEditable(false);
        jTextAreaReceive.setBackground(new java.awt.Color(153, 255, 255));
        jTextAreaReceive.setColumns(20);
        jTextAreaReceive.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextAreaReceive.setLineWrap(true);
        jTextAreaReceive.setRows(5);
        jTextAreaReceive.setWrapStyleWord(true);
        jTextAreaReceive.setEnabled(false);
        jScrollPane1.setViewportView(jTextAreaReceive);

        jTextAreaSend.setBackground(new java.awt.Color(153, 255, 255));
        jTextAreaSend.setColumns(20);
        jTextAreaSend.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextAreaSend.setLineWrap(true);
        jTextAreaSend.setRows(5);
        jTextAreaSend.setWrapStyleWord(true);
        jTextAreaSend.setEnabled(false);
        jTextAreaSend.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAreaSendKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextAreaSendKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTextAreaSend);

        jButtonLimpar.setText("Limpar");
        jButtonLimpar.setEnabled(false);
        jButtonLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparActionPerformed(evt);
            }
        });

        jButtonEnviar.setText("Enviar");
        jButtonEnviar.setEnabled(false);
        jButtonEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEnviarActionPerformed(evt);
            }
        });

        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonLimpar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonLimpar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConectarActionPerformed

    }//GEN-LAST:event_jButtonConectarActionPerformed

    private void jButtonSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSairActionPerformed
        ChatMessage message = new ChatMessage();
        message.setName(this.message.getName());
        message.setAction(Action.DISCONNECT);
        this.service.send(message);
        disconnected();
    }//GEN-LAST:event_jButtonSairActionPerformed

    private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed
        this.jTextAreaSend.setText("");
    }//GEN-LAST:event_jButtonLimparActionPerformed

    private void jButtonEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnviarActionPerformed
        sendMessage();
    }//GEN-LAST:event_jButtonEnviarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String text = this.jTextAreaSend.getText();
        String name = this.message.getName();
        if (!text.isEmpty()) {
            this.message = new ChatMessage();
            this.message.setName(name);
            this.message.setText(text);
            this.message.setAction(Action.SEND_ALL);
            
            this.jTextAreaReceive.append("Você disse: " + text + "\n");
            
            this.service.send(this.message);
        }
        this.jTextAreaSend.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped

    private void jTextAreaSendKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaSendKeyTyped

    }//GEN-LAST:event_jTextAreaSendKeyTyped

    private void jTextAreaSendKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaSendKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage();
            evt.consume();
        }
    }//GEN-LAST:event_jTextAreaSendKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonConectar;
    private javax.swing.JButton jButtonEnviar;
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonSair;
    private javax.swing.JList jListOnline;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextAreaReceive;
    private javax.swing.JTextArea jTextAreaSend;
    private javax.swing.JTextField jTextFieldTextName;
    // End of variables declaration//GEN-END:variables
}
