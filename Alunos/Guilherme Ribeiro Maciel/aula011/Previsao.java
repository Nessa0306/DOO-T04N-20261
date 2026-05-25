package weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Previsao {
	float temp;
	float tempmin;
	float tempmax;
	float humidity;
	@JsonProperty("conditions")
	String condition;
	float precip;
	float winddir;
	float windspeed;
	String resolvedAddress;
	
	public Previsao() {
		
	}

	public String getResolvedAddress() {
		return resolvedAddress;
	}

	public void setResolvedAddress(String resolvedAddress) {
		this.resolvedAddress = resolvedAddress;
	}
	
	public float getTemp() {
		return temp;
	}
	
	public void setTemp(float temp) {
		this.temp = temp;
	}
	
	public float getTempmin() {
		return tempmin;
	}
	
	public void setTempmin(float tempmin) {
		this.tempmin = tempmin;
	}
	
	public float getTempmax() {
		return tempmax;
	}
	
	public void setTempmax(float tempmax) {
		this.tempmax = tempmax;
	}
	
	public float getHumidity() {
		return humidity;
	}
	
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	
	public String getCondition() {
		return condition;
	}
	
	public void setConditions(String condition) {
		this.condition = condition;
	}
	
	public float getPrecip() {
		return precip;
	}
	
	public void setPrecip(float precip) {
		this.precip = precip;
	}
	
	public float getWinddir() {
		return winddir;
	}
	
	public void setWinddir(float winddir) {
		this.winddir = winddir;
	}
	
	public float getWindspeed() {
		return windspeed;
	}
	
	public void setWindspeed(float windspeed) {
		this.windspeed = windspeed;
	}
	
	public String resumo() {
		return "Cidade: " + resolvedAddress + ", Temperatura Atual: " + temp + ", Maxima do dia: " + tempmax 
				+ ", Minima do dia: " + tempmin + ", Humidade: " + humidity + ", Condição do tempo: " + condition 
				+ ", Precipitação de Chuva: " + precip + ", Velocidade do Vento: " + windspeed + ", Direção do Vento: " 
				+ winddir;
	}
}
