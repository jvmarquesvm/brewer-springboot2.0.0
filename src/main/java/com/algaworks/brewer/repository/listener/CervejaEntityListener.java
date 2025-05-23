package com.algaworks.brewer.repository.listener;

import javax.persistence.PostLoad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.algaworks.brewer.BrewerApplication;
import com.algaworks.brewer.controller.storage.FotoStorage;
import com.algaworks.brewer.model.Cerveja;

public class CervejaEntityListener {
	//@Autowired
	//private FotoStorage fotoStorage;
	
	@PostLoad
	public void postLoad(final Cerveja cerveja ) {
		
		//FotoStorage não vai existir pois o Entity é iniciado pelo Hibernate
		//Esta classe resolve todas as injeções antes de usar na classe baseado no contexto atual
		//Ao modificar de SpringFram5 para Springboot2 está acontecendo que esta classe como está no 
		//contexto de hibernate, não consegue modificar para o contexto do spring
		//SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		FotoStorage fotoStorage = BrewerApplication.getBean(FotoStorage.class);
		cerveja.setUrlFoto(fotoStorage.getUrl(cerveja.getFotoOrMock()));
		cerveja.setUrlThumbnailFoto(fotoStorage.getUrl(fotoStorage.THUMB_PREFIX + cerveja.getFotoOrMock() ));
	}
}
