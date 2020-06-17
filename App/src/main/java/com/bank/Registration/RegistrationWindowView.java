package com.bank.Registration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class RegistrationWindowView {
    private RegistrationWindowPresenter presenter;
    private Pane parent;
    Button registration_button;
    Button back_button;
    TextField login_field;
    PasswordField password_field1;
    PasswordField password_field2;
    TextField email_field;
    TextField name_field;
    TextField last_name_field;
    TextField phone_field;
    
    public RegistrationWindowView(Pane pane) {
        this.parent = pane;
    }

    public void setPresenter(RegistrationWindowPresenter presenter) {
        this.presenter = presenter;
    }

    public void showCreator(){
        registration_button = new Button("Rejestracja");
        back_button = new Button("Wstecz");
        login_field = new TextField();
        password_field1 = new PasswordField();
        password_field2 = new PasswordField();
        email_field = new TextField();
        name_field = new TextField();
        last_name_field = new TextField();
        phone_field = new TextField();

        registration_button.setOnMouseClicked(event -> {
            presenter.registration();
        });
        back_button.setOnMouseClicked(event ->presenter.backToLoginWindow());

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.addRow(0, new Label("Login:"), login_field);
        gridPane.addRow(1, new Label("Hasło:"), password_field1);
        gridPane.addRow(2, new Label("Hasło:"), password_field2);
        gridPane.addRow(3, new Label("Imie:"), name_field);
        gridPane.addRow(4, new Label("Nazwisko:"), last_name_field);
        gridPane.addRow(5, new Label("Nr_tel:"), phone_field);
        gridPane.addRow(6, new Label("Mejl:"), email_field);
        gridPane.addRow(7, registration_button,back_button);
        parent.getChildren().clear();
        parent.getChildren().add(gridPane);

    }

    protected Pane getParent(){ return parent; }
    protected TextField getLogin(){return  login_field;}
    protected TextField getPassword1(){return  password_field1;}
    protected TextField getPassword2(){return  password_field2;}
    protected TextField getName(){return  name_field;}
    protected TextField getLastName(){return  last_name_field;}
    protected TextField getPhone(){return  phone_field;}
    protected TextField getMail(){return  email_field;}
}
