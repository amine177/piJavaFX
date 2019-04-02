/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.overpowered.pijavafx.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import tn.esprit.overpowered.byusforus.entities.entrepriseprofile.JobOffer;
import tn.esprit.overpowered.byusforus.entities.users.CompanyAdmin;
import tn.esprit.overpowered.byusforus.entities.users.HRManager;
import tn.esprit.overpowered.byusforus.entities.users.ProjectManager;
import tn.esprit.overpowered.byusforus.services.entrepriseprofile.JobOfferFacadeRemote;
import util.authentication.Authenticator;
import util.information.tracker.InfoTracker;
import util.routers.FXRouter;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class OffersController implements Initializable {

    @FXML
    private AnchorPane parentAnchorPane;
    @FXML
    private AnchorPane rightMenuAnchorPane;
    @FXML
    private Button profileButton;
    @FXML
    private AnchorPane topMenuAnchorPane;
    @FXML
    private MenuBar topMenu;
    @FXML
    private TableView<JobOffer> jobsView;
    @FXML
    private TableColumn<JobOffer, String> title;
    @FXML
    private TableColumn<JobOffer, String> offerStatus;
    @FXML
    private TableColumn<JobOffer, String> dateOfCreation;
    @FXML
    private TableColumn<JobOffer, String> city;
    @FXML
    private TableColumn<JobOffer, String> dateOfArchive;
    @FXML
    private TableColumn<JobOffer, String> peopleNeeded;
    @FXML
    private JFXButton viewProfile;
    @FXML
    private Label status;
    @FXML
    private Button searchButton;
    @FXML
    private Button newOfferButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button myProfileButton;
    @FXML
    private TableColumn<JobOffer, String> quiz;
    private Context context;
    @FXML
    private AnchorPane centerAnchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        FXRouter.when("CreateJobOfferView", "CreateJobOffer.fxml", "JobOffer", 640, 425);
        FXRouter.setRouteContainer("CreateJobOfferView", parentAnchorPane);
        FXRouter.when("CompanyHRProfileView", "CompanyHRProfile.fxml", "Profile", 600, 400);
        FXRouter.setRouteContainer("CompanyHRProfileView", parentAnchorPane);
        FXRouter.when("CompanyPMProfileView", "CompanyPMProfile.fxml", "Profile", 600, 400);
        FXRouter.setRouteContainer("CompanyPMProfileView", parentAnchorPane);
        FXRouter.when("BaseView", "Base.fxml", "HOME", 800, 600);
        FXRouter.setRouteContainer("BaseView", parentAnchorPane);
        FXRouter.when("ManageQuiz", "ManageQuiz.fxml");
        FXRouter.setRouteContainer("ManageQuiz", centerAnchorPane);
        try {
            String jndiName = "piJEE-ejb-1.0/JobOfferFacade!tn.esprit.overpowered.byusforus.services.entrepriseprofile.JobOfferFacadeRemote";
            context = new InitialContext();
            JobOfferFacadeRemote jobOfferProxy = (JobOfferFacadeRemote) context.lookup(jndiName);
            List<JobOffer> list = jobOfferProxy.viewAllOffers();
            if (list.isEmpty()) {
                System.out.println("EMPTYYYYYYYYYYYYYYYYYYYY");
            }
            //System.out.println("THE LOCATION ISSSSSSSSSS: " + list.get(0).getCity());
            ObservableList<JobOffer> offerObs = FXCollections.observableArrayList();

            for (JobOffer o : list) {
                offerObs.add(o);
            }
            title.setCellValueFactory(new PropertyValueFactory<>("Title"));
            offerStatus.setCellValueFactory(new PropertyValueFactory<>("offerStatus"));
            dateOfCreation.setCellValueFactory(new PropertyValueFactory<>("dateOfCreation"));
            city.setCellValueFactory(new PropertyValueFactory<>("city"));
            dateOfArchive.setCellValueFactory(new PropertyValueFactory<>("dateOfArchive"));
            peopleNeeded.setCellValueFactory(new PropertyValueFactory<>("peopleNeeded"));
            Callback<TableColumn<JobOffer, String>, TableCell<JobOffer, String>> cellFactory
                    = //
                    new Callback<TableColumn<JobOffer, String>, TableCell<JobOffer, String>>() {
                @Override
                public TableCell call(final TableColumn<JobOffer, String> param) {
                    final TableCell<JobOffer, String> cell = new TableCell<JobOffer, String>() {

                        final Button btn = new Button("Manage Quiz");

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                btn.setOnAction(event -> {
                                    JobOffer jobOffer = getTableView().getItems().get(getIndex());
                                    System.out.println("Quiz btn clicked for job offer" + jobOffer.getTitle());
                                    Map<Context, JobOffer> dataMap = new HashMap<>();
                                    dataMap.put(context, jobOffer);
                                    try {
                                        FXRouter.goTo("ManageQuiz", dataMap);
                                    } catch (IOException ex) {
                                        Logger.getLogger(OffersController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
                                setGraphic(btn);
                                setText(null);
                            }
                        }
                    };
                    return cell;
                }
            };
            System.out.println("Still working at this point");
            quiz.setCellFactory(cellFactory);
            jobsView.setItems(offerObs);
//            jobsView.getColumns().add(quiz);

        } catch (NamingException ex) {
            Logger.getLogger(CandidateListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void profileButtonClicked(MouseEvent event) {
    }

    @FXML
    private void selected(MouseEvent event) {
    }

    @FXML
    private void viewProfileAction(MouseEvent event) {
    }

    @FXML
    private void searchButtonClicked(MouseEvent event) {
    }

    @FXML
    private void newOfferButtonOnclicked(MouseEvent event) throws IOException {
        FXRouter.goTo("CreateJobOfferView");
    }

    @FXML
    private void homeButtonOnClicked(MouseEvent event) throws IOException {
        FXRouter.goTo("BaseView");
    }

    @FXML
    private void myProfileButtonOnClicked(MouseEvent event) throws IOException, NamingException {
        String type = Authenticator.currentUser.getDiscriminatorValue();
        Long currentUserId = Authenticator.currentUser.getId();
        switch (type) {
            case "COMPANY_ADMIN":
                CompanyAdmin compAdmin = InfoTracker.getAdminInformation(currentUserId);
                FXRouter.goTo("CompanyAdminProfileView", compAdmin);
                break;
            case "HUMAN_RESOURCES_MANAGER":
                System.out.println("THIS IS UR IDDDDDDDDDDD  " + currentUserId);
                HRManager hrManager = InfoTracker.getHRInformation(currentUserId);
                FXRouter.goTo("CompanyHRProfileView", hrManager);
                break;
            case "PROJECT_MANAGER":
                ProjectManager pManager = InfoTracker.getPMInformation(currentUserId);
                FXRouter.goTo("CompanyPMProfileView", pManager);
                break;
            default:
                break;
        }

    }

}
