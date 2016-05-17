package io.vertx.example;

import java.util.logging.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;

/**
 * @author Santhosh Kani
 */
public class EventConsumer extends AbstractVerticle {
    private static final Logger LOG = Logger.getLogger(EventConsumer.class.getName());

    /**
     * 
     */
    public EventConsumer() {
    }

    /* (non-Javadoc)
     * @see io.vertx.core.AbstractVerticle#start()
     */
    @Override
    public void start() throws Exception {

        final EventBus eb = vertx.eventBus();

        MessageConsumer<String> messageConsumer = eb.consumer("message-queue");
        messageConsumer.handler(new Handler<Message<String>>() {

            @Override
            public void handle(Message<String> event) {
                LOG.info(deploymentID() + " instance received => " + event.body());
            }
        });

        messageConsumer.completionHandler(new Handler<AsyncResult<Void>>() {
            @Override
            public void handle(AsyncResult<Void> event) {
                if (event.succeeded()) {
                    LOG.info("The handler has been registered with all nodes.");
                } else {
                    LOG.warning("Handler registration failed. " + event.cause().getMessage());
                }
            }
        });
    }

}
