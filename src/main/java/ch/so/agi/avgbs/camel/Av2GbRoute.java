package ch.so.agi.avgbs.camel;

import java.nio.ByteBuffer;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.s3.S3Constants;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.so.agi.camel.predicates.IlivalidatorPredicate;

@Component
public class Av2GbRoute extends RouteBuilder {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${app.ftpUserInfogrips}")
    private String ftpUserInfogrips;

    @Value("${app.ftpPwdInfogrips}")
    private String ftpPwdInfogrips;

    @Value("${app.ftpUrlInfogrips}")
    private String ftpUrlInfogrips;

    @Value("${app.pathToAv2GbDownloadFolder}")
    private String pathToDownloadFolder;

    @Value("${app.pathToAv2GbUnzipFolder}")
    private String pathToUnzipFolder;
    
    @Value("${app.pathToAv2GbErrorFolder}")
    private String pathToErrorFolder;
    
    @Value("${app.awsAccessKey}")
    private String awsAccessKey;

    @Value("${app.awsSecretKey}")
    private String awsSecretKey;
    
    @Value("${app.awsBucketNameAv2Gb}")
    private String awsBucketName;
    
    @Value("${app.dbHostEdit}")
    private String dbHostEdit;
    
    @Value("${app.dbDatabaseEdit}")
    private String dbDatabaseEdit;
    
    @Value("${app.dbSchemaEdit}")
    private String dbSchemaEdit;

    @Value("${app.dbUserEdit}")
    private String dbUserEdit;

    @Value("${app.dbPwdEdit}")
    private String dbPwdEdit;
    
    @Value("${app.downloadDelayAv2Gb}")
    private String downloadDelay;

    @Value("${app.uploadDelayAv2Gb}")
    private String uploadDelay;

    @Value("${app.importDelayAv2Gb}")
    private String importDelay;

    @Value("${app.initialDownloadDelayAv2Gb}")
    private String initialDownloadDelay;

    @Value("${app.initialUploadDelayAv2Gb}")
    private String initialUploadDelay;

    @Value("${app.initialImportDelayAv2Gb}")
    private String initialImportDelay;

    @Override
    public void configure() throws Exception {
        // Download file from Infogrips ftp server.
        from("ftp://"+ftpUserInfogrips+"@"+ftpUrlInfogrips+"/\\av2gb\\?password="+ftpPwdInfogrips+"&antInclude=*.zip&autoCreate=false&noop=true&readLock=changed&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay="+downloadDelay+"&initialDelay=5000&idempotentRepository=#fileConsumerRepo&idempotentKey=av2gb-ftp-${file:name}-${file:size}-${file:modified}")
        .to("file://"+pathToDownloadFolder)
        .split(new ZipSplitter())
        .streaming().convertBodyTo(ByteBuffer.class)
            .choice()
                .when(body().isNotNull())
                    .to("file://"+pathToUnzipFolder) 
            .end()
        .end();        

        // Upload file to S3.
        from("file://"+pathToUnzipFolder+"/?noop=true&delay=30000&initialDelay="+uploadDelay+"&readLock=changed&idempotentRepository=#fileConsumerRepo&idempotentKey=av2gb-s3-${file:name}-${file:size}-${file:modified}")
        .convertBodyTo(byte[].class)
        .setHeader(S3Constants.CONTENT_LENGTH, simple("${in.header.CamelFileLength}"))
        .setHeader(S3Constants.KEY,simple("${in.header.CamelFileNameOnly}"))
        .setHeader(S3Constants.CANNED_ACL,simple("PublicRead")) 
        .to("aws-s3://" + awsBucketName
                + "?deleteAfterWrite=false&region=EU_CENTRAL_1" 
                + "&accessKey={{awsAccessKey}}"
                + "&secretKey=RAW({{awsSecretKey}})")
        .log(LoggingLevel.INFO, "File uploaded: ${in.header.CamelFileNameOnly}");
        
        // Import file into database.s.
//        IlivalidatorPredicate isValid = new IlivalidatorPredicate();
//        
//        from("file://"+pathToUnzipFolder+"/?noop=true&include=.*\\.xml&delay="+importDelay+"&initialDelay=7000&readLock=changed&idempotentRepository=#fileConsumerRepo&idempotentKey=ili2pg-${file:name}-${file:size}-${file:modified}")
//        .choice()
//            .when(isValid).toD("ili2pg:import?dbhost="+dbHostEdit+"&dbport=5432&dbdatabase="+dbDatabaseEdit+"&dbschema="+dbSchemaEdit+"&dbusr="+dbUserEdit+"&dbpwd="+dbPwdEdit+"&dataset=${file:onlyname.noext}")
//            .otherwise().to("file://"+pathToErrorFolder)
//        .end();
    }
}
