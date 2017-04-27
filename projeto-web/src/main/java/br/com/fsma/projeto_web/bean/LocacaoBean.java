package br.com.fsma.projeto_web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import br.com.fsma.projeto_web.modelo.dao.ClienteDao;
import br.com.fsma.projeto_web.modelo.dao.LocacaoDao;
import br.com.fsma.projeto_web.modelo.negocio.Cliente;
import br.com.fsma.projeto_web.modelo.negocio.Locacao;
import br.com.fsma.projeto_web.tx.Transacional;

@Named
@ViewScoped
public class LocacaoBean implements Serializable {
	/**
	 * criado por Ezequiel
	 */

	@Inject
	private LocacaoDao locacaoDao;
	@Inject
	private ClienteDao clienteDao;
	@Inject
	private Cliente cliente = new Cliente();
	@Inject
	private Locacao locacao;

	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {
		System.out.println("LocacaoBean.init();");
		locacao = new Locacao();
	}

	@Transacional
	public String iniciarCadastroLocacao() {
		return "/view/cadastro/cadastrolocacao.xhtml?faces-redirect=true";
	}

	@Transacional
	public String iniciaalteracaoLocacao() {
		return "/view/alteracao/alteracaolocacao.xhtml?faces-redirect=true";
	}

	@Transacional
	public String limpar() {
		locacao = new Locacao();
		return null;
	}

	@Transacional
	public List<Cliente> listarClientes() {
		List<Cliente> clientebuscaDao = clienteDao.listaTodosPaginada(0, 100);
		return clientebuscaDao;
	}


	@Transacional
	public String incluirLocacao() {
		try{
			locacaoDao.adiciona(locacao);
			mensagemSucesso("Cadastrado com sucesso");
			limpar();
			return null;
		
		}catch (Exception e) {
			mensagemErro("erro ao incluir locacao");
			return null;
				}
		}
	
	

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Transacional
	public String atualizaCliente() {
		clienteDao.atualiza(cliente);
		System.out.println("encontrado" + cliente.getNome());
		mensagemSucesso("Atualizada Corretamente!");
		cliente = new Cliente();
		return null;
	}
	@Transacional
	public String excluirCliente() {
		try {
			clienteDao.remove(cliente);
			mensagemSucesso ("Excluido corretamente!");
		} catch (Exception e) {
			mensagemErro("Não foi possivel Excluir");
		}
		limpar();
		return null;
	}

	@Transacional
	public List<Cliente> clientessCadastrados() {
		List<Cliente> clientes = new ArrayList<Cliente>();
		clientes = clienteDao.listaTodosPaginada(0, 100);
		return clientes;
	}
	
	@Transacional
	public String checarCnpj() {
		Cliente clientebuscaDao = clienteDao.buscaPorCnpj(cliente.getCnpj());
		if (clientebuscaDao == null) {
			mensagemErro("Cliente não foi encontrado verifique o CNPJ digitado");			
            return null;
            }
		System.out.println(clientebuscaDao.getCnpj());
		cliente = clientebuscaDao;
		return null;
	}

	private void mensagemSucesso(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!!", mensagem));
	}

	private void mensagemErro(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", mensagem));

	}

}
