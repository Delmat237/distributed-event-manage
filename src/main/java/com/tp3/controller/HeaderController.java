package com.tp3.controller;


import com.tp3.service.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public   class HeaderController {
    @FXML private  Button dashboardBtn,logoutBtn,loginBtn;

    private final NavigationService navigationService = NavigationService.getInstance();


    public void initialize(){
        if (LoginController.roleCurrentUser == null) //s'il n'est pas encore logger
            dashboardBtn.setOnAction(e -> navigationService.goTo("LoginView.fxml"));
        else if (LoginController.roleCurrentUser.equals("Organisateur"))
           dashboardBtn.setOnAction(e -> navigationService.goTo("DashboardOrganisateurView.fxml"));
        else if (LoginController.roleCurrentUser.equals("Participant"))
            dashboardBtn.setOnAction(e -> navigationService.goTo("DashboardParticipantView.fxml"));

        loginBtn.setOnAction(e ->navigationService.goTo("LoginView.fxml"));
        logoutBtn.setOnAction(e ->navigationService.goTo("AcceuilView.fxml"));
    }
}