package app.bvk.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.bvk.entity.Creature;
import app.bvk.utils.ZoomableImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class UtilitiesPane extends BorderPane
{

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilitiesPane.class);

    @FXML
    private Button d4Button;
    @FXML
    private Button d6Button;
    @FXML
    private Button d8Button;
    @FXML
    private Button d10Button;
    @FXML
    private Button d12Button;
    @FXML
    private Button d20Button;

    @FXML
    private ChoiceBox<Integer> amountChoiceBox;

    @FXML
    private Label resultLabel;

    @FXML
    private BorderPane creatureBorderPane;

    public UtilitiesPane()
    {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("UtilitiesGui.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try
        {
            loader.load();
        }
        catch (final IOException e)
        {
            LOGGER.error("ERROR while loading encounterentry fxml", e);
        }
    }

    @FXML
    public void initialize()
    {
        this.setupChoiceBox();
        this.setupButtons();
    }

    public void updateCreaturePreview(final Creature creature)
    {
        this.creatureBorderPane.setCenter(new ZoomableImageView(this.creatureBorderPane, creature.getImage()));
    }

    private void setupChoiceBox()
    {
        final ObservableList<Integer> diceAmounts = FXCollections.observableArrayList();
        for (int index = 1; index < 11; index++)
        {
            diceAmounts.add(index);
        }
        this.amountChoiceBox.setItems(diceAmounts);
        this.amountChoiceBox.getSelectionModel().select(0);
    }

    private void setupButtons()
    {
        this.d4Button.setOnAction(event ->
        {
            final List<Integer> results = this.rollDice(4, this.amountChoiceBox.getValue());
            this.displayResults(results);
        });
        this.d6Button.setOnAction(event ->
        {
            final List<Integer> results = this.rollDice(6, this.amountChoiceBox.getValue());
            this.displayResults(results);
        });
        this.d8Button.setOnAction(event ->
        {
            final List<Integer> results = this.rollDice(8, this.amountChoiceBox.getValue());
            this.displayResults(results);
        });
        this.d10Button.setOnAction(event ->
        {
            final List<Integer> results = this.rollDice(10, this.amountChoiceBox.getValue());
            this.displayResults(results);
        });
        this.d12Button.setOnAction(event ->
        {
            final List<Integer> results = this.rollDice(12, this.amountChoiceBox.getValue());
            this.displayResults(results);
        });
        this.d20Button.setOnAction(event ->
        {
            final List<Integer> results = this.rollDice(20, this.amountChoiceBox.getValue());
            this.displayResults(results);
        });
    }

    private void displayResults(final List<Integer> results)
    {
        final int sumOfResults = results.stream().mapToInt(Integer::intValue).sum();
        final StringBuilder builder = new StringBuilder();
        for (int index = 0; index < results.size(); index++)
        {
            if (index == 0)
            {
                builder.append(results.get(index));
            }
            else if (index == results.size() - 1)
            {
                builder.append("+" + results.get(index) + "\n= " + sumOfResults);
            }
            else
            {
                builder.append("+" + results.get(index));
            }
        }
        this.resultLabel.setText(builder.toString());
    }

    private List<Integer> rollDice(final int dieBase, final int amount)
    {
        final List<Integer> individualResults = new ArrayList<>();
        final Random random = new Random();
        for (int index = 0; index < amount; index++)
        {
            individualResults.add(random.nextInt(dieBase) + 1);
        }
        return individualResults;
    }

}
