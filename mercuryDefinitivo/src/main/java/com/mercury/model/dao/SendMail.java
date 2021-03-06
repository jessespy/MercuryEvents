package com.mercury.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.mercury.model.dao.SmtpAutenticazione;

public class SendMail {
	Connection conn = null;
	private final String host = "smtp.sincrono.it";
    private final String mittente = "corso.java@sincrono.it(Mercury Newsletter)";
	String oggetto = "";
	private final String user="corso.java@sincrono.it";
	private final String psw ="c0rs0.java";

	
	public void sendEmail(int cadenza) throws SQLException {
		
		if(conn==null) conn=DAO.getConnection();
        Statement st1 = conn.createStatement();
        Statement st2 = conn.createStatement();

        Properties p = System.getProperties();
	
		p.setProperty("mail.smtp.host", this.host);
	    p.put("mail.smtp.host", this.host);
	    p.put("mail.debug", "true");
	    p.put("mail.smtp.auth", "true"); 
	   
	     
	    Session sessione = Session.getDefaultInstance(p, new SmtpAutenticazione(user, psw) );
	    sessione.setPasswordAuthentication(new URLName("smtp", host, 25, "INBOX", user, psw), new PasswordAuthentication(user, psw));
	     
	    //MimeMessage mail = new MimeMessage(sessione);

        String query="SELECT * FROM mercury.utente where idCadenza = " + cadenza;	
        
        // esegui query 
        ResultSet rs = st1.executeQuery(query);
		
		if(cadenza==1) { oggetto = "Invio newsLetter giornaliera Mercury"; }
		if(cadenza==2) { oggetto = "Invio newsLetter settimanale Mercury"; }
		if(cadenza==3) { oggetto = "Invio newsLetter mensile Mercury"; }
	
	    String testo = "Questi sono i possimi eventi scelti in base alle tue preferenze: \n\n";
	     
	    String dest = "";
	    try {
	    	 while(rs.next()) {
	    		 MimeMessage mail = new MimeMessage(sessione);
	    		
	    		 dest = rs.getString("emailUtente");
	    		 int idUtente = rs.getInt("idUtente");
	    		 
	    		 String nuovaQuery = "select nomeEvento, dataInizio, dataFine, descrizione from utente u, preftipo p, eventoPrevisto e ";
	    		 nuovaQuery += "where u.idUtente = p.idUtente and u.idUtente = "+idUtente+" and p.idTipoEvento = e.idTipoEvento";
	    		 
	    		 ResultSet rs2 = st2.executeQuery(nuovaQuery);
	    		 String testoEventi = "";
	    		 while(rs2.next()) {
	    			 String nomeEvento = rs2.getString("nomeEvento");
	    			 String dataInizio = rs2.getString("dataInizio");
	    			 String dataFine   = rs2.getString("dataFine");
	    			 String desc       = rs2.getString("descrizione");
	            	
	    			 testoEventi += ("- "+nomeEvento+" \n  inzier� il: '"+dataInizio+"' e terminer� il: '"+dataFine+"' \n  descrizione evento:"+desc+"\n\n");
	    		}
	    		testoEventi += "\n\nGrazie per aver scelto il servizio di newsletter di Mercury.";
	            
	    	 	mail.setFrom(new InternetAddress(mittente));
	    	 	mail.addRecipients(Message.RecipientType.TO, dest);
	    	 	
	    	 	mail.setSubject(oggetto);
	    	 	mail.setText(testo+testoEventi);
	    	 	
	    	 	Transport tr = sessione.getTransport("smtp");
				tr.connect(host, user, psw);
	    	 	Transport.send(mail, mail.getAllRecipients());
	    	 }	
	     }
	    catch(Exception e) {
	    	 	e.printStackTrace();
	     }
	}
}
