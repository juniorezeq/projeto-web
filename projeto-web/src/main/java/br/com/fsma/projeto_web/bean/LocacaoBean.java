package br.com.fsma.projeto_web.bean;

import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
	private Locacao locacao;
	@Inject
	private Cliente cliente = new Cliente();
	private Date dataInicio;
	private Date dataFim;
	private double valorDiaria;
	int dias;

	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {
		System.out.println("LocacaoBean.init();");
		limpar();		
	}

	@Transacional
	public String iniciarCadastroLocacao() {
		return "/view/cadastro/cadastrolocacao.xhtml?faces-redirect=true";
	}
	
	
	public String CalcularDiarias(){
		locacao.converterDataInicio(dataInicio);
		locacao.converterDataFim(dataFim);
		dias = locacao.quantidadeDias();
		return null;
	}
	
	
	public String calcularValorTotal(){
		locacao.converterDataInicio(dataInicio);
		locacao.converterDataFim(dataFim);		
		dias = locacao.quantidadeDias();
		locacao.setValorTotal(valorDiaria*dias);
		return null;
	}

	@Transacional
	public String iniciaalteracaoLocacao() {
		return "/view/alteracao/alteracaolocacao.xhtml?faces-redirect=true";
	}

	
	public String limpar() {
		locacao = new Locacao();
		valorDiaria = 0.00;
		Date dataAtual =  new Date(System.currentTimeMillis());
		locacao.converterDataInicio(dataAtual);
		locacao.converterDataFim(dataAtual);
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
			locacao.setValorDiaria(valorDiaria);
			locacao.converterDataInicio(dataInicio);
			locacao.converterDataFim(dataFim);
			locacao.setValorTotal(valorDiaria*dias);
			locacao.setCliente(cliente);
		    locacao.setDataRegistro(LocalDate.now());
		    System.out.println(locacao.getDataRegistro());
		    locacaoDao.adiciona(locacao);
			mensagemSucesso("Cadastrado com sucesso!  " +  locacao.quantidadeDias() + " dias");
			limpar();
			return null;
		}catch (Exception e) {
			mensagemErro("erro ao incluir locacao");
			return null;
				}
	
		}
	


	public double getValorDiaria() {
		return valorDiaria;
	}

	public void setValorDiaria(double valorDiaria) {
		this.valorDiaria = valorDiaria;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Locacao getLocacao() {
		return locacao;
	}

	public void setLocacao(Locacao locacao) {
		this.locacao = locacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
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
