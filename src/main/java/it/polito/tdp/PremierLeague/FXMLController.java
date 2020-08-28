/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.db.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Integer> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<?> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<?> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	txtResult.clear();
    	Integer min = null ;
    	try {
    		min = Integer.parseInt(txtMinuti.getText());
    	}catch (NumberFormatException e) {
			txtResult.appendText("Errore nell'inserimento del MIN!\n" );
			return;
		}
    	Integer mese = this.cmbMese.getValue();
    	if(mese== null) {
    		txtResult.appendText("Devi prima scegliere il mese!\n");
    		return;
    	}
    	Map<Integer, Match> idMap = this.model.getMap();
    	txtResult.appendText("Coppie con connessione massima: \n");
    	List<Adiacenza> collegamenti = this.model.getCollegamenti(mese, min, idMap);
    	int pesoMax = collegamenti.get(0).getPeso(); 
    	for(Adiacenza a : collegamenti) {
    		if(a.getPeso() == pesoMax) {
    			txtResult.appendText(a + "\n");
    		}
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Integer min = null ;
    	try {
    		min = Integer.parseInt(txtMinuti.getText());
    	}catch (NumberFormatException e) {
			txtResult.appendText("Errore nell'inserimento del MIN!\n" );
			return;
		}
    	Integer mese = this.cmbMese.getValue();
    	if(mese== null) {
    		txtResult.appendText("Devi prima scegliere il mese!\n");
    		return;
    	}
    	Map<Integer, Match> idMap = this.model.getMap();
    	this.model.creaGrafo(mese, idMap);
    	txtResult.appendText(String.format("Creato grafo con %d vertici e %d archi!", this.model.numVertici(), this.model.numArchi()));
    	
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbMese.getItems().addAll(this.model.getMesi());
    }
    
    
}
