package br.com.fsma.projeto_web.modelo.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.fsma.projeto_web.modelo.negocio.Cliente;
import br.com.fsma.projeto_web.modelo.negocio.Locacao;



@Named
@RequestScoped
public class LocacaoDao implements Serializable {
	private static final long serialVersionUID = 1L;

	private DAO<Locacao> dao;
	@PostConstruct
	void init() {
		this.dao = new DAO<Locacao>(this.em, Locacao.class);
	}

	@Inject
	private EntityManager em;
	
	
public void adiciona(Locacao locacao) {
	dao.adiciona(locacao);
}

public void atualiza(Locacao locacao){
	em.merge(locacao);
}

public void remove(Locacao locacao) {
	dao.remove(locacao);
}

public Locacao buscaPorId(Long id) {
	return dao.buscaPorId(id);
}

public List<Locacao> listaTodosPaginada(int firstResult, int maxResults) {
	return dao.listaTodosPaginada(firstResult, maxResults);
}

public List<Locacao> buscaPorCnpj(Cliente cliente) {
	TypedQuery<Locacao> query = em.createQuery(
			  " select u from locacao u "
			+ " where u.cliente = :pCliente", Locacao.class);
	
	query.setParameter("pCLiente", cliente);
	try {
		List<Locacao> resultado = query.getResultList();
	     return resultado;
	} catch (NoResultException ex) {
		return null;
	}
}
	
}