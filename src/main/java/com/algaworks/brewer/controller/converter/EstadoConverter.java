package com.algaworks.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Estado;

//Cada converter criado deve registrar ele no WebConfig.java
@Component
public class EstadoConverter implements Converter<String, Estado> {

	@Override
	public Estado convert(String codigo) {
		
		if(!StringUtils.isEmpty(codigo)) {
			System.out.println(">>> Cadastro Estado: Estado:codigo> " + codigo );
			Estado estado = new Estado();
			estado.setCodigo(Long.valueOf(codigo));
			return estado;
		}
		return null;
	}

}
