function giorniNelMese(mese) {
	if(mese==0 || mese==2 || mese==4 || mese==6 || mese==7 || mese==9 || mese==11)
		return 31;
	else if(mese==1)
		return 28;
	else
		return 30;
}

function controlloBello(form) {
		
	if(form.dataInizio.value=="" || form.dataFine.value==""){
		alert("Inserire una data valida");
		return false;
	}
	
	var today = new Date();
	var nGio = giorniNelMese(today.getMonth());
	var a = (today.getDate()+7) % nGio;
	
	var dataIn = new Date(form.dataInizio.value); 
	var dataFin = new Date(form.dataFine.value);
	if((a >= dataIn.getDate() && today.getMonth() >= dataIn.getMonth()) && today.getYear() >= dataIn.getYear()){
		alert("Inserire una data valida");
		return false;
	}
	else if(dataIn.getMonth() < today.getMonth() && dataIn.getYear() < today.getYear()) {
		alert("Inserire una data valida");
		return false;
	}
	else if(dataIn.getYear() < today.getYear()) {
		alert("Inserire una data valida");
		return false;
	}
	
	if(dataFin < dataIn) {
		alert("Inserire una data di fine valida");
		return false;
	}
	
	 if(form.nomeEvento.value == "") {
	      alert("Inserire un nome per l'evento");
	      return false;
	}
	 else  if(form.descEvento.value == "") {
	      alert("Inserire una descrizione");
	      return false;
	}
	 else if(form.tipoEvento.value == "opt") {
	      alert("Inserire una categoria per l'evento");
	      return false;
	}
	 if(form.idComune.value=="Selezionare"){
		 alert("Inserire un comune");
	     return false;
	 }
	 
	return true;
	
}