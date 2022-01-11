package brightspot.meal;

import java.util.List;
import java.util.stream.Collectors;

import brightspot.module.list.page.PagePromo;
import brightspot.page.AbstractContentPageViewModel;
import brightspot.recipe.HasRecipesData;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.view.PageEntryView;
import com.psddev.dari.db.Query;
import com.psddev.styleguide.meal.MealPageView;
import com.psddev.styleguide.meal.MealPageViewCoursesField;
import com.psddev.styleguide.meal.MealPageViewLeadField;
import com.psddev.styleguide.meal.MealPageViewMealDescriptionField;
import com.psddev.styleguide.meal.MealPageViewMealTitleField;
import com.psddev.styleguide.meal.MealPageViewSimilarMealsField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

public class MealPageViewModel extends AbstractContentPageViewModel<Meal> implements
    MealPageView,
    PageEntryView {

    // --- MealPageView support ---

    @Override
    public Iterable<? extends MealPageViewCoursesField> getCourses() {
        return createViews(MealPageViewCoursesField.class, model.getCourses());
    }

    @Override
    public Iterable<? extends MealPageViewLeadField> getLead() {
        return createViews(MealPageViewLeadField.class, model.getLead());
    }

    @Override
    public Iterable<? extends MealPageViewMealDescriptionField> getMealDescription() {
        return RichTextUtils.buildHtml(
            model,
            Meal::getDescription,
            e -> createView(MealPageViewMealDescriptionField.class, e));
    }

    @Override
    public Iterable<? extends MealPageViewMealTitleField> getMealTitle() {
        return RichTextUtils.buildInlineHtml(
            model,
            Meal::getTitle,
            e -> createView(MealPageViewMealTitleField.class, e));
    }

    @Override
    public Iterable<? extends MealPageViewSimilarMealsField> getSimilarMeals() {
        List<PagePromo> similarMeals = Query.from(Meal.class)
            .where(HasRecipesData.RECIPES_FIELD + " = ?", model.getRecipes())
            .and("_id != ?", model)
            .sortDescending(Content.PUBLISH_DATE_FIELD)
            .select(0, 10)
            .getItems()
            .stream()
            .map(PagePromo::fromPromotable)
            .collect(Collectors.toList());
        return createViews(MealPageViewSimilarMealsField.class, similarMeals);
    }

    // --- PageView support ---

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }
}
