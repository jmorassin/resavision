package com.jmdev.resavision.cucumber.stepdefs;

import com.jmdev.resavision.ResavisionApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ResavisionApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
