package afterwind.lab1.ui.control;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class BetterPagination extends HBox {

    public Button buttonPrevious;
    public Button buttonNext;

    private int currentPage = 0;
    private int maxPages = 0;
    private Consumer<Integer> pageChange;

    public BetterPagination() {
    }

    public void init(Consumer<Integer> pageChange, int maxPages) {
        this.pageChange = pageChange;
        this.maxPages = maxPages;
        buttonPrevious = new Button("<");
        buttonNext= new Button(">");

        buttonPrevious.setOnAction(this::handleButtonClick);
        buttonNext.setOnAction(this::handleButtonClick);

        getChildren().addAll(buttonPrevious, buttonNext);
        setAlignment(Pos.CENTER);
        disableBasedOnPages();
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
        if (currentPage >= maxPages) {
            setCurrentPage(maxPages - 1);
        }
        disableBasedOnPages();
    }

    public void setCurrentPage(int page) {
        currentPage = page;
        pageChange.accept(currentPage);
        disableBasedOnPages();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    private void handleButtonClick(ActionEvent ev) {
        if (ev.getSource() == buttonNext) {
            currentPage++;
        } else {
            currentPage--;
        }
        pageChange.accept(currentPage);
        disableBasedOnPages();
    }

    private void disableBasedOnPages() {
        buttonPrevious.setDisable(currentPage == 0);
        buttonNext.setDisable(currentPage == maxPages - 1);
    }
}
