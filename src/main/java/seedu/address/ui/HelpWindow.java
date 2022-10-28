package seedu.address.ui;

import java.awt.Desktop;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2223s1-cs2103-f13-4.github.io/tp/UserGuide.html";
    public static final String GITHUB_URL = "https://github.com/AY2223S1-CS2103-F13-4/tp";
    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";
    private static final double DURATION_FOR_TEXT_ANIMATION = 0.1;
    private static final String PATIENT_SEARCH_TEXT = "Add Patient";
    private static final String ADD_DEMO_INPUT_PATIENT = "add c/P n/John Doe g/M p/98765432 e/johnd@example.com "
            + "a/311, Clementi Ave 2, #02-25 t/friends t/owesMoney dt/2022-11-11T12:30 v/True";
    private static final String ADD_DEMO_OUTPUT_PATIENT = "New Patient added: Category: P Uid: 10; Name: John Doe;"
            + " Gender: M; Phone: 98765432; Email: johnd@example.com; Address: 311, Clementi Ave 2, #02-25;"
            + " Tags: [owesMoney][friends]; Home Visits Date and Time:11/11/2022 12:30 , ; Visit Status: visited";
    private static final String NURSE_SEARCH_TEXT = "Add Nurse";
    private static final String ADD_DEMO_INPUT_NURSE = "add c/N n/Cola t/Pediatric e/cola@example.com g/F p/98345432"
            + " a/Blk 431 Ang Mo Kio Ave 10, Singapore 560431 #01-03 t/heartDiseaseSpecialist";
    private static final String ADD_DEMO_OUTPUT_NURSE = "New person added: Category: N Uid: 3; Name: Cola;"
            + " Gender: F; Phone: 98345432; Email: cola@example.com; "
            + "Address: Blk 431 Ang Mo Kio Ave 10, Singapore 560431 #01-03; Tags: [heartDiseaseSpecialist][Pediatric]";
    private static final String ADD_PATIENT_USAGE_HELP = "Add Patient \n"
            + "add c/Category n/Name g/Gender p/Phone e/Email a/Address v/Visted "
            + "[t/tag] [dt/Date&Time (2022-11-11T12:30)] *[] is optional. ";
    private static final String ADD_NURSE_USAGE_HELP = "Add Nurse\n"
            + "add c/Category n/Name g/Gender p/Phone e/Email a/Address v/Visited "
            + "[t/tag] *[] is optional. ";
    private static final String CLEAR_SEARCH_TEXT = "Clear All";
    private static final String CLEAR_DEMO_INPUT = "clear";
    private static final String CLEAR_DEMO_OUTPUT = "Address book has been cleared!";
    private static final String DELETE_SEARCH_TEXT = "Delete via ID";
    private static final String DELETE_DEMO_INPUT = "delete id/3";
    private static final String DELETE_DEMO_OUTPUT = "Deleted person: Category: N Uid: 3; Name: Cola; Gender: F;"
            + " Phone: 98345432; Email: cola@example.com;"
            + " Address: Blk 431 Ang Mo Kio Ave 10, Singapore 560431 #01-03; Tags: [heartDiseaseSpecialist][Pediatric]";
    private static final String EDIT_NAME_SEARCH_TEXT = "Edit Name";
    private static final String EDIT_NAME_DEMO_INPUT = "edit id/12 n/Kola";
    private static final String EDIT_NAME_DEMO_OUTPUT = "Edited person: Category: N Uid: 12; Name: Kola; Gender: F;"
            + " Phone: 98345432; Email: cola@example.com; Address: Blk 431 Ang Mo Kio Ave 10, Singapore 560431 #01-03;"
            + " Tags: [heartDiseaseSpecialist][Pediatric]";
    private static final String EDIT_GENDER_SEARCH_TEXT = "Edit Gender";
    private static final String EDIT_GENDER_DEMO_INPUT = "edit id/12 g/M";
    private static final String EDIT_GENDER_DEMO_OUTPUT = "Edited person: Category: N Uid: 12; Name: Kola;"
            + " Gender: M; Phone: 98345432; Email: cola@example.com;"
            + " Address: Blk 431 Ang Mo Kio Ave 10, Singapore 560431 #01-03; Tags: [heartDiseaseSpecialist][Pediatric]";
    private static final String EDIT_PHONE_SEARCH_TEXT = "Edit Phone";
    private static final String EDIT_PHONE_DEMO_INPUT = "edit id/12 p/88888888";
    private static final String EDIT_PHONE_DEMO_OUTPUT = "Edited person: Category: N Uid: 12; Name: Kola; Gender: M;"
            + " Phone: 88888888; Email: cola@example.com; Address: Blk 431 Ang Mo Kio Ave 10, Singapore 560431 #01-03;"
            + " Tags: [heartDiseaseSpecialist][Pediatric]";
    private static final String EDIT_EMAIL_SEARCH_TEXT = "Edit Email";
    private static final String EDIT_EMAIL_DEMO_INPUT = "edit id/12 e/Kola@example.com";
    private static final String EDIT_EMAIL_DEMO_OUTPUT = "Edited person: Category: N Uid: 12; Name: Kola; Gender: M;"
            + " Phone: 88888888; Email: Kola@example.com; Address: Blk 431 Ang Mo Kio Ave 10, Singapore 560431 #01-03;"
            + " Tags: [heartDiseaseSpecialist][Pediatric]";
    private static final String EDIT_ADDRESS_SEARCH_TEXT = "Edit Address";
    private static final String EDIT_ADDRESS_DEMO_INPUT = "edit id/12 a/Blk 768 Woodlands Ave 6, Singapore 730768 ";
    private static final String EDIT_ADDRESS_DEMO_OUTPUT = "Edited person: Category: N Uid: 12; Name: Kola;"
            + " Gender: M; Phone: 88888888; Email: Kola@example.com;"
            + " Address: Blk 768 Woodlands Ave 6, Singapore 730768; Tags: [heartDiseaseSpecialist][Pediatric]";
    private static final String EDIT_TAG_SEARCH_TEXT = "Edit Tag";
    private static final String EDIT_TAG_DEMO_INPUT = "edit id/12 t/Pediatric";
    private static final String EDIT_TAG_DEMO_OUTPUT = "Edited person: Category: N Uid: 12; Name: Kola; Gender: M;"
            + " Phone: 88888888; Email: Kola@example.com; Address: Blk 768 Woodlands Ave 6, Singapore 730768;"
            + " Tags: [Pediatric]";
    private static final String EDIT_MIX_SEARCH_TEXT = "Edit Mix";
    private static final String EDIT_MIX_DEMO_INPUT = "edit id/12 g/F p/98345432 "
            + "a/Blk 431 Ang Mo Kio Ave 10, Singapore 560431 #01-03";
    private static final String EDIT_MIX_DEMO_OUTPUT = "dited person: Category: N Uid: 12; Name: Kola; Gender: F;"
            + " Phone: 98345432; Email: Kola@example.com; Address: Blk 431 Ang Mo Kio Ave 10, Singapore 560431 #01-03;"
            + " Tags: [Pediatric]";
    private static final String EXIT_SEARCH_TEXT = "Exit Program";
    private static final String EXIT_DEMO_INPUT = "exit";
    private static final String EXIT_DEMO_OUTPUT = "Thank you for using Healthcare Xpress!";
    private static final String FIND_SEARCH_TEXT = "Find via Name";
    private static final String FIND_DEMO_INPUT = "find Kola";
    private static final String FIND_DEMO_OUTPUT = "1 patients and nurses listed!";
    private static final String HELP_SEARCH_TEXT = "Help";
    private static final String HELP_DEMO_INPUT = "help";
    private static final String HELP_DEMO_OUTPUT = "*Brings you to this window*";
    private static final String LIST_NTH_SEARCH_TEXT = "List (Default)";
    private static final String LIST_NTH_DEMO_INPUT = "list";
    private static final String LIST_NTH_DEMO_OUTPUT = "Listed all persons with specifications: "
            + "ADDRESS: NIL, CATEGORY: NIL, GENDER: NIL, TAG: NIL, FULLY ASSIGNED: NIL, FULLY VISITED: NIL";
    private static final String LIST_ADDRESS_SEARCH_TEXT = "List all with X Address";
    private static final String LIST_ADDRESS_DEMO_INPUT = "list a/431";
    private static final String LIST_ADDRESS_DEMO_OUTPUT = "Listed all persons with specifications: "
            + "ADDRESS: 431, CATEGORY: NIL, GENDER: NIL, TAG: NIL, FULLY ASSIGNED: NIL, FULLY VISITED: NIL";
    private static final String LIST_CATEGORY_SEARCH_TEXT = "List all with X Category";
    private static final String LIST_CATEGORY_DEMO_INPUT = "list c/N";
    private static final String LIST_CATEGORY_DEMO_OUTPUT = "Listed all persons with specifications: "
            + "ADDRESS: NIL, CATEGORY: N, GENDER: NIL, TAG: NIL, FULLY ASSIGNED: NIL, FULLY VISITED: NIL";
    private static final String LIST_GENDER_SEARCH_TEXT = "List all with X Gender";
    private static final String LIST_GENDER_DEMO_INPUT = "list g/F";
    private static final String LIST_GENDER_DEMO_OUTPUT = "Listed all persons with specifications: "
            + "ADDRESS: NIL, CATEGORY: NIL, GENDER: F, TAG: NIL, FULLY ASSIGNED: NIL, FULLY VISITED: NIL";
    private static final String LIST_TAG_SEARCH_TEXT = "List all with X Tag";
    private static final String LIST_TAG_DEMO_INPUT = "list t/Pediatric";
    private static final String LIST_TAG_DEMO_OUTPUT = "Listed all persons with specifications: "
            + "ADDRESS: NIL, CATEGORY: NIL, GENDER: NIL, TAG: Pediatric, FULLY ASSIGNED: NIL, FULLY VISITED: NIL";
    private static final String LIST_FULLY_ASSIGNED_SEARCH_TEXT = "List all that are fully assigned";
    private static final String LIST_FULLY_ASSIGNED_DEMO_INPUT = "list as/true";
    private static final String LIST_FULLY_ASSIGNED_DEMO_OUTPUT = "Listed all persons with specification: "
            + "ADDRESS: NIL, CATEGORY: NIL, GENDER: NIL, TAG: NIL, FULLY ASSIGNED: true, FULLY VISITED: NIL";
    private static final String LIST_FULLY_VISITED_SEARCH_TEXT = "List all patients that have been fully visited or " +
            "nurses that have completed all assigned visits";
    private static final String LIST_FULLY_VISITED_DEMO_INPUT = "list v/true";
    private static final String LIST_FULLY_VISITED_DEMO_OUTPUT = "Listed all persons with specification: "
            + "ADDRESS: NIL, CATEGORY: NIL, GENDER: NIL, TAG: NIL, FULLY ASSIGNED: NIL, FULLY VISITED: true";
    private static final String LIST_MIX_SEARCH_TEXT = "List (Mix)";
    private static final String LIST_MIX_DEMO_INPUT = "list a/431 g/F";
    private static final String LIST_MIX_DEMO_OUTPUT = "Listed all persons with specifications: "
            + "ADDRESS: 431, CATEGORY: NIL, GENDER: F, TAG: NIL, FULLY ASSIGNED: NIL, FULLY VISITED: NIL";
    private static final String CLEAR_USAGE_HELP = "";
    private static final String DELETE_USAGE_HELP = "Delete Person from list: \n"
            + "delete id/ID ";
    private static final String EDIT_USAGE_HELP = "Edit Person from list: \n"
            + "edit [c/Category] [n/Name] [g/gender] [p/Phone] [e/Email] [a/Address] [t/Tags] [dt/Date&Time]"
            + "*[] is optional.";
    private static final String EXIT_USAGE_HELP = "";
    private static final String FIND_USAGE_HELP = "Find a person from the list that contains keyword\n"
            + "find KEYWORD";
    private static final String HELP_USAGE_HELP = "";
    private static final String LIST_USAGE_HELP = "List all that meets the critiera:\n"
            + "list [c/Category] [g/Gender] [a/Address] [t/Tag]\n"
            + "*[] is optional";
    private static final String MARK_USAGE_HELP = "Mark a patient visited:\n"
            + "mark id/ID";

    private final List<String> commandList = Arrays.asList(PATIENT_SEARCH_TEXT, NURSE_SEARCH_TEXT, CLEAR_SEARCH_TEXT,
            DELETE_SEARCH_TEXT, EDIT_NAME_SEARCH_TEXT, EDIT_GENDER_SEARCH_TEXT, EDIT_PHONE_SEARCH_TEXT,
            EDIT_EMAIL_SEARCH_TEXT, EDIT_TAG_SEARCH_TEXT, EDIT_MIX_SEARCH_TEXT, EXIT_SEARCH_TEXT,
            FIND_SEARCH_TEXT, HELP_SEARCH_TEXT, LIST_NTH_SEARCH_TEXT, LIST_ADDRESS_SEARCH_TEXT,
            LIST_CATEGORY_SEARCH_TEXT, LIST_GENDER_SEARCH_TEXT, LIST_TAG_SEARCH_TEXT, LIST_FULLY_ASSIGNED_SEARCH_TEXT,
            LIST_FULLY_VISITED_SEARCH_TEXT, LIST_MIX_SEARCH_TEXT);
    private final HashMap<String, String[]> dictionary = new HashMap<String, String[]>();
    private final HashMap<String, String> dictionaryForUsageHelp = new HashMap<String, String>();

    private CommandNameListPanel commandNameListPanel;
    private Timeline currentTimeLine;

    @FXML
    private VBox resultDisplayPlaceholder;

    @FXML
    private TextArea tipTextArea;
    @FXML
    private TextField searchTextField;

    @FXML
    private TextField helpCommandTextField;

    @FXML
    private TextField helpOutputTextField;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        init();
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Initialize HelpWindow, dictionary and add listener to ListView.
     */
    public void init() {
        initDictionary();
        initDictionaryForUsageHelp();
        String emptyStr = "";
        String[] result = searchResult(emptyStr);
        ObservableList<String> obvListResult = convertStrArrToObservableList(result);
        fillResultDisplay(obvListResult);
        addListenerToListView();
    }

    /**
     * Initialize dictionary.
     */
    public void initDictionary() {
        dictionary.put(PATIENT_SEARCH_TEXT, new String[]{ADD_DEMO_INPUT_PATIENT, ADD_DEMO_OUTPUT_PATIENT});
        dictionary.put(NURSE_SEARCH_TEXT, new String[]{ADD_DEMO_INPUT_NURSE, ADD_DEMO_OUTPUT_NURSE});
        dictionary.put(CLEAR_SEARCH_TEXT, new String[]{CLEAR_DEMO_INPUT, CLEAR_DEMO_OUTPUT});
        dictionary.put(DELETE_SEARCH_TEXT, new String[]{DELETE_DEMO_INPUT, DELETE_DEMO_OUTPUT});
        dictionary.put(EDIT_NAME_SEARCH_TEXT, new String[]{EDIT_NAME_DEMO_INPUT, EDIT_NAME_DEMO_OUTPUT});
        dictionary.put(EDIT_GENDER_SEARCH_TEXT, new String[]{EDIT_GENDER_DEMO_INPUT, EDIT_GENDER_DEMO_OUTPUT});
        dictionary.put(EDIT_PHONE_SEARCH_TEXT, new String[]{EDIT_PHONE_DEMO_INPUT, EDIT_PHONE_DEMO_OUTPUT});
        dictionary.put(EDIT_EMAIL_SEARCH_TEXT, new String[]{EDIT_EMAIL_DEMO_INPUT, EDIT_EMAIL_DEMO_OUTPUT});
        dictionary.put(EDIT_ADDRESS_SEARCH_TEXT, new String[]{EDIT_ADDRESS_DEMO_INPUT, EDIT_ADDRESS_DEMO_OUTPUT});
        dictionary.put(EDIT_TAG_SEARCH_TEXT, new String[]{EDIT_TAG_DEMO_INPUT, EDIT_TAG_DEMO_OUTPUT});
        dictionary.put(EDIT_MIX_SEARCH_TEXT, new String[]{EDIT_MIX_DEMO_INPUT, EDIT_MIX_DEMO_OUTPUT});
        dictionary.put(EXIT_SEARCH_TEXT, new String[]{EXIT_DEMO_INPUT, EXIT_DEMO_OUTPUT});
        dictionary.put(FIND_SEARCH_TEXT, new String[]{FIND_DEMO_INPUT, FIND_DEMO_OUTPUT});
        dictionary.put(HELP_SEARCH_TEXT, new String[]{HELP_DEMO_INPUT, HELP_DEMO_OUTPUT});
        dictionary.put(LIST_NTH_SEARCH_TEXT, new String[]{LIST_NTH_DEMO_INPUT, LIST_NTH_DEMO_OUTPUT});
        dictionary.put(LIST_ADDRESS_SEARCH_TEXT, new String[]{LIST_ADDRESS_DEMO_INPUT, LIST_ADDRESS_DEMO_OUTPUT});
        dictionary.put(LIST_CATEGORY_SEARCH_TEXT, new String[]{LIST_CATEGORY_DEMO_INPUT, LIST_CATEGORY_DEMO_OUTPUT});
        dictionary.put(LIST_GENDER_SEARCH_TEXT, new String[]{LIST_GENDER_DEMO_INPUT, LIST_GENDER_DEMO_OUTPUT});
        dictionary.put(LIST_TAG_SEARCH_TEXT, new String[]{LIST_TAG_DEMO_INPUT, LIST_TAG_DEMO_OUTPUT});
        dictionary.put(LIST_FULLY_ASSIGNED_SEARCH_TEXT, new String[]{LIST_FULLY_ASSIGNED_DEMO_INPUT,
                LIST_FULLY_ASSIGNED_DEMO_OUTPUT});
        dictionary.put(LIST_FULLY_VISITED_SEARCH_TEXT, new String[]{LIST_FULLY_VISITED_DEMO_INPUT,
                LIST_FULLY_VISITED_DEMO_OUTPUT});
        dictionary.put(LIST_MIX_SEARCH_TEXT, new String[]{LIST_MIX_DEMO_INPUT, LIST_MIX_DEMO_OUTPUT});
    };

    /**
     * Initialize dictionary for usage help.
     */
    public void initDictionaryForUsageHelp() {
        dictionaryForUsageHelp.put(PATIENT_SEARCH_TEXT, ADD_PATIENT_USAGE_HELP);
        dictionaryForUsageHelp.put(NURSE_SEARCH_TEXT, ADD_NURSE_USAGE_HELP);
        dictionaryForUsageHelp.put(CLEAR_SEARCH_TEXT, CLEAR_USAGE_HELP);
        dictionaryForUsageHelp.put(DELETE_SEARCH_TEXT, DELETE_USAGE_HELP);
        dictionaryForUsageHelp.put(EDIT_NAME_SEARCH_TEXT, EDIT_USAGE_HELP);
        dictionaryForUsageHelp.put(EDIT_GENDER_SEARCH_TEXT, EDIT_USAGE_HELP);
        dictionaryForUsageHelp.put(EDIT_PHONE_SEARCH_TEXT, EDIT_USAGE_HELP);
        dictionaryForUsageHelp.put(EDIT_EMAIL_SEARCH_TEXT, EDIT_USAGE_HELP);
        dictionaryForUsageHelp.put(EDIT_ADDRESS_SEARCH_TEXT, EDIT_USAGE_HELP);
        dictionaryForUsageHelp.put(EDIT_TAG_SEARCH_TEXT, EDIT_USAGE_HELP);
        dictionaryForUsageHelp.put(EDIT_MIX_SEARCH_TEXT, EDIT_USAGE_HELP);
        dictionaryForUsageHelp.put(EXIT_SEARCH_TEXT, EXIT_USAGE_HELP);
        dictionaryForUsageHelp.put(FIND_SEARCH_TEXT, FIND_USAGE_HELP);
        dictionaryForUsageHelp.put(HELP_SEARCH_TEXT, HELP_USAGE_HELP);
        dictionaryForUsageHelp.put(LIST_NTH_SEARCH_TEXT, LIST_USAGE_HELP);
        dictionaryForUsageHelp.put(LIST_ADDRESS_SEARCH_TEXT, LIST_USAGE_HELP);
        dictionaryForUsageHelp.put(LIST_CATEGORY_SEARCH_TEXT, LIST_USAGE_HELP);
        dictionaryForUsageHelp.put(LIST_GENDER_SEARCH_TEXT, LIST_USAGE_HELP);
        dictionaryForUsageHelp.put(LIST_TAG_SEARCH_TEXT, LIST_USAGE_HELP);
        dictionaryForUsageHelp.put(LIST_FULLY_ASSIGNED_SEARCH_TEXT, LIST_USAGE_HELP);
        dictionaryForUsageHelp.put(LIST_FULLY_VISITED_SEARCH_TEXT, LIST_USAGE_HELP);
        dictionaryForUsageHelp.put(LIST_MIX_SEARCH_TEXT, LIST_USAGE_HELP);
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException
     *                               <ul>
     *                               <li>
     *                               if this method is called on a thread other than
     *                               the JavaFX Application Thread.
     *                               </li>
     *                               <li>
     *                               if this method is called during animation or
     *                               layout processing.
     *                               </li>
     *                               <li>
     *                               if this method is called on the primary stage.
     *                               </li>
     *                               <li>
     *                               if {@code dialogStage} is already showing.
     *                               </li>
     *                               </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Searches the command list with keyword str.
     *
     * @param str Keyword.
     * @return An array of strings that fits the criteria.
     */
    public String[] searchResult(String str) {
        return commandList.stream().filter(x -> x.contains(str)).toArray(String[]::new);
    }

    /**
     * Populates the display in HelpWindow.
     *
     * @param input An ObservableList of Strings.
     */
    public void fillResultDisplay(ObservableList<String> input) {
        commandNameListPanel = new CommandNameListPanel(input);
        resultDisplayPlaceholder.getChildren().add(commandNameListPanel.getRoot());
    }

    /**
     * Converts String array to ObservableList.
     *
     * @param strArr String array to be converted.
     * @return An Observable List.
     */
    public ObservableList<String> convertStrArrToObservableList(String[] strArr) {
        return FXCollections.observableArrayList(strArr);
    }

    /**
     * Adds listener to ListView to detect changes. If changes is detected, plays
     * the corresponding animation.
     */
    public void addListenerToListView() {
        ListView<String> listView = commandNameListPanel.getCommandNameListView();
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                stopAnimationIfAny();
                String[] inputOutput = dictionary.get(newValue);
                helpOutputTextField.clear();
                animateTextInTextField(inputOutput, helpCommandTextField, false);
                String usageHelp = dictionaryForUsageHelp.get(newValue);
                tipTextArea.setText(usageHelp);
            }
        });
    }

    /**
     * Stops any ongoing animation.
     */
    public void stopAnimationIfAny() {
        if (currentTimeLine != null) {
            currentTimeLine.stop();
        }
    }

    /**
     * Animates text in textfield.
     *
     * @param inputOutput A String array containing the input and output result.
     * @param txtField    The javafx textfield to be animated on.
     * @param isOutput    Check if it is on input's or on output's textfield.
     */
    public void animateTextInTextField(String[] inputOutput, TextField txtField, boolean isOutput) {
        String str = inputOutput[isOutput ? 1 : 0];
        final IntegerProperty counter = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(DURATION_FOR_TEXT_ANIMATION),
                event -> {
                    if (counter.get() > str.length()) {
                        timeline.stop();
                        if (!isOutput) {
                            animateTextInTextField(inputOutput, helpOutputTextField, true);
                        }
                    } else {
                        txtField.setText(str.substring(0, counter.get()));
                        txtField.positionCaret(counter.get());
                        counter.set(counter.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        currentTimeLine = timeline;
    }

    /**
     * Sends user to GitHub page.
     */
    @FXML
    public void visitGitHubButtonAction() {
        try {
            Desktop.getDesktop().browse(new URI(GITHUB_URL));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends user to user guide page.
     */
    @FXML
    public void visitHelpButtonAction() {
        try {
            Desktop.getDesktop().browse(new URI(USERGUIDE_URL));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles and show accordingly to User the filtered List View according to
     * User's search keyword.
     */
    @FXML
    public void handleSearchKeywordEntered() {
        String searchText = searchTextField.getText().trim();
        resultDisplayPlaceholder.getChildren().clear();
        String[] result = searchResult(searchText);
        ObservableList<String> obvListResult = convertStrArrToObservableList(result);
        fillResultDisplay(obvListResult);
        addListenerToListView();
    }

}
