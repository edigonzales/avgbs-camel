package ch.so.agi.avgbs;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.processor.idempotent.FileIdempotentRepository;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CamelContext camelContext;

    @Value("${app.idempotentFileUrl}")
    private String idempotentFileUrl;

    @Bean
    public FileIdempotentRepository fileConsumerRepo() {
        FileIdempotentRepository fileConsumerRepo = null;
        try {
            fileConsumerRepo = new FileIdempotentRepository();
            fileConsumerRepo.setFileStore(new File(idempotentFileUrl));
            fileConsumerRepo.setCacheSize(5000);
            fileConsumerRepo.setMaxFileStoreSize(51200000);
        } catch (Exception e) {
            log.error("Caught exception inside Creating fileConsumerRepo ..." + e.getMessage());
        }
        if (fileConsumerRepo == null) {
            log.error("fileConsumerRepo == null ...");
        }
        return fileConsumerRepo;
    }

//    @Bean
//    public TomcatServletWebServerFactory tomcatFactory() {
//      return new TomcatServletWebServerFactory() {
//        @Override
//        protected void postProcessContext(Context context) {
//          ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
//        }
//      };
//    }
}
