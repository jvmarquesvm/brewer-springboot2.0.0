package com.algaworks.brewer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.EstiloRepository;
import com.algaworks.brewer.service.exception.ImpossivelExcluirEntidadeException;
import com.algaworks.brewer.service.exception.NomeEstiloCadastroException;

@Service
public class CadastroEstiloService {
	
	@Autowired
	EstiloRepository estiloRepository;
	
	@Transactional
	public Estilo salvar(Estilo estilo) {
		if(estilo.getCodigo() == null || nomeDoEstiloMudou(estilo)) {
			Optional<Estilo> estiloOptional = estiloRepository.findByNomeIgnoreCase(estilo.getNome());
			
			if ( estiloOptional.isPresent() ) {
				throw new NomeEstiloCadastroException("Nome do estilo já cadastrado");
			}
		}
		return estiloRepository.saveAndFlush(estilo);
	}
	
	@Transactional
	public void excluir(Estilo estilo) {
		try {
			this.estiloRepository.delete(estilo);
			this.estiloRepository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar estilo. Já está atrelado a alguma cerveja.");
		}
	}
	
	private boolean nomeDoEstiloMudou(Estilo estilo) {
		final String nomeNovo = estilo.getNome();
		final String nomeAntigo = this.estiloRepository.findByCodigo(estilo.getCodigo()).getNome();
		return !nomeAntigo.equalsIgnoreCase(nomeNovo);
	}

}
