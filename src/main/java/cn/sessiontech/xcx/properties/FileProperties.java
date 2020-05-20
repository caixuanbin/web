package cn.sessiontech.xcx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="file")
public class FileProperties {
	
	private String physicalPath;
	private String networkPath;

}
