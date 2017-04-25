package br.com.fsma.projeto_web.bean.ws;

import br.com.fsma.projeto_web.modelo.webservice.CNPJ_RWS;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitApiInterface {
	
	
	
	@GET("v1/cnpj/{cnpj}")
	Call<CNPJ_RWS> getCNPJ(@Path("cnpj") String cnpj); 

}
