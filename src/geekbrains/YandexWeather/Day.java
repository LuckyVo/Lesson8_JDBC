package geekbrains.YandexWeather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Day {
    @JsonProperty("temp_avg")
    private Long tempAvg;
    private WindDir windDir;
    @JsonProperty("condition")
    private Condition condition;

    public Long getTempAvg() { return tempAvg; }
    public void setTempAvg(Long value) { this.tempAvg = value; }

    public WindDir getWindDir() { return windDir; }
    public void setWindDir(WindDir value) { this.windDir = value; }

    public Condition getCondition() { return condition; }
    public void setCondition(Condition value) { this.condition = value; }
}
