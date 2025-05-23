package com.algaworks.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.repository.helper.cerveja.VendaRepositoryQueries;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long>, VendaRepositoryQueries {

}
