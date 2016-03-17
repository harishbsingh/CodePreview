package com.org;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MyAutomatorService extends Application<MyAutomatorServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new MyAutomatorService().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<MyAutomatorServiceConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(MyAutomatorServiceConfiguration conf, Environment environment) throws Exception {
        //Thread Pool
        AutomatorThreadPool automatorThreadPool = new AutomatorThreadPool(conf.getThreadPool().getCorePoolSize());
        Emailer emailer = new Emailer(conf, automatorThreadPool);
        environment.lifecycle().manage(automatorThreadPool);

    }
}
