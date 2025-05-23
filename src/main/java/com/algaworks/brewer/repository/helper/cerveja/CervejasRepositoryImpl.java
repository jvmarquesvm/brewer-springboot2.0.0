package com.algaworks.brewer.repository.helper.cerveja;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.controller.storage.FotoStorage;
import com.algaworks.brewer.dto.CervejaDTO;
import com.algaworks.brewer.dto.ValorItensEstoque;
import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.filter.CervejaFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

/**
 * 
 * @author joaovictor
 * CervejasRepositoryImpl
 * Nome base CervejasRepository
 * complemento obrigatorio Impl
 * configuravel 
 * 
 * @EnableJpaRepositories(
 * repositoryImplementationPostfix = "Impl" no JPAConfig {
 *
 */
public class CervejasRepositoryImpl implements CervejasRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	PaginacaoUtil paginacaoUtil;
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
		
		/* PaginacaoUtil.class
		 * 
		 * //Configurando a Paginação int totalRegistrosPorPagina =
		 * pageable.getPageSize(); int paginaAtual = pageable.getPageNumber(); int
		 * primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		 * 
		 * criteria.setFirstResult(primeiroRegistro);
		 * criteria.setMaxResults(totalRegistrosPorPagina);
		 * 
		 * //Ordenação Sort sort = pageable.getSort();
		 * System.out.println("Ordenação Tipo: " + sort);
		 * 
		 * if(sort != null) { //Ordenando apenas por 1 campo, mas pode ser vários
		 * Sort.Order order = sort.iterator().next(); String property =
		 * order.getProperty();
		 * 
		 * criteria.addOrder(order.isAscending() ? Order.asc(property) :
		 * Order.desc(property) ); }
		 */
		
		paginacaoUtil.preparar(criteria, pageable);
		
		adicionarFiltro(filtro, criteria);
		//return criteria.list();
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	private void adicionarFiltro(CervejaFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if(!StringUtils.isEmpty(filtro.getSku())) {
				criteria.add(Restrictions.eq("sku", filtro.getSku()));
			}
			
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}

			if (isEstiloPresente(filtro)) {
				criteria.add(Restrictions.eq("estilo", filtro.getEstilo()));
			}

			if (filtro.getSabor() != null) {
				criteria.add(Restrictions.eq("sabor", filtro.getSabor()));
			}

			if (filtro.getOrigem() != null) {
				criteria.add(Restrictions.eq("origem", filtro.getOrigem()));
			}

			if (filtro.getValorDe() != null) {
				criteria.add(Restrictions.ge("valor", filtro.getValorDe()));
			}

			if (filtro.getValorAte() != null) {
				criteria.add(Restrictions.le("valor", filtro.getValorAte()));
			}
		}
	}
	
	private Long total(CervejaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
		//Contando o total de registro para o filtro especificado
		// Isso vai fazer o paginador mostrar corretamente o numero de pagina
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private boolean isEstiloPresente(CervejaFilter filtro) {
		return filtro.getEstilo() != null && filtro.getEstilo().getCodigo() != null;
	}

	@Override
	public List<CervejaDTO> porSkuOuNome(String skuOuNome) {
		String jpql = "select new com.algaworks.brewer.dto.CervejaDTO(codigo, sku, nome, origem, valor, foto) " 
				                  + "from Cerveja where lower(sku) like lower(:skuOuNome) or lower(nome) like lower(:skuOuNome)";
		List<CervejaDTO> cervejasFiltradas = manager.createQuery(jpql, CervejaDTO.class)
				                                   .setParameter("skuOuNome", skuOuNome + "%")
				                                   .getResultList();
		//Para usar no S3
		cervejasFiltradas.forEach(cerveja -> 
		                cerveja.setUrlThumbnailFoto(fotoStorage.getUrl(fotoStorage.THUMB_PREFIX + cerveja.getFoto())));
		return cervejasFiltradas;
	}
	
	@Override
	public ValorItensEstoque valorItensEstoque() {
		String query = "select new com.algaworks.brewer.dto.ValorItensEstoque(sum(valor * quantidadeEstoque), sum(quantidadeEstoque)) from Cerveja";
		return manager.createQuery(query, ValorItensEstoque.class).getSingleResult();
	}
	
}
