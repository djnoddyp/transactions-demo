package com.pnodder.demo;

import com.pnodder.demo.config.AppConfig;
import com.pnodder.demo.model.Bike;
import com.pnodder.demo.model.Style;
import com.pnodder.demo.services.BikeService;
import com.pnodder.demo.services.Service;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        Bike bikeOne = Bike.of("GT", "Avalanche 1.0", "Silver", Style.MOUNTAIN);
        Bike bikeTwo = Bike.of("Specialized", "Roubaix", "Red", Style.ROAD);
        Service bikeService = ctx.getBean(BikeService.class);
        bikeService.insert(bikeOne);
        bikeService.insert(bikeTwo);
        for (Bike b : bikeService.findAll()) {
            System.out.println(b);
        }
    }

}
