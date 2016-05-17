package io.vertx.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * @author Santhosh Kani
 */
@Component
public class Sender implements InitializingBean, DisposableBean {
    private static final Logger LOG = Logger.getLogger(Sender.class.getName());
    private final Vertx vertx;
    private final List<String> deploymentIds = new ArrayList<>();
    private static final String SENDER_COUNT = "vertx.sender.count";

    @Autowired
    private Environment environment;

    @Autowired
    public Sender(Vertx vertx) {
        this.vertx = vertx;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        Iterator<String> iterator = deploymentIds.iterator();
        while (iterator.hasNext()) {
            final String deploymentId = iterator.next();
            vertx.undeploy(deploymentId, new Handler<AsyncResult<Void>>() {

                @Override
                public void handle(AsyncResult<Void> event) {
                    if (event.succeeded()) {
                        LOG.info("Sender instance " + deploymentId + " undeployed successfully");
                    } else {
                        LOG.warning("Failed to undeploy the verticle " + deploymentId);
                    }
                }
            });
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        Integer senderCount = null;
        try {
            senderCount = Integer.parseInt(environment.getProperty(SENDER_COUNT));
        } catch (NumberFormatException exc) {
            senderCount = 1;
        }

        int i = 0;
        while (i < senderCount.intValue()) {
            vertx.deployVerticle(EventGenerator.class.getName(), new Handler<AsyncResult<String>>() {

                @Override
                public void handle(AsyncResult<String> event) {
                    if (event.succeeded()) {
                        String deploymentId = event.result();
                        LOG.info("Sender instance initialized successfully. Deployment ID: " + deploymentId);
                        deploymentIds.add(deploymentId);
                    } else {
                        LOG.warning("Failed to deploy the Sender instance.");
                    }
                }
            });
            i++;
        }
    }

}
