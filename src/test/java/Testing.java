import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;


import static org.junit.Assert.*;



@RunWith(Parameterized.class)
public class Testing {


    private Burger burger;
    private Bun bun;
    private Ingredient ingredient1;
    private Ingredient ingredient2;


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {IngredientType.SAUCE, IngredientType.SAUCE},
                {IngredientType.FILLING, IngredientType.FILLING},
                {IngredientType.SAUCE, IngredientType.FILLING},
                {IngredientType.FILLING, IngredientType.SAUCE}
        });
    }

    @Parameterized.Parameter(0)
    public IngredientType type1;

    @Parameterized.Parameter(1)
    public IngredientType type2;

    @Before
    public void makeBurger() {
        burger = new Burger();


        bun = Mockito.mock(Bun.class);
        Mockito.when(bun.getName()).thenReturn("white bun");
        Mockito.when(bun.getPrice()).thenReturn(200f);


        ingredient1 = Mockito.mock(Ingredient.class);
        Mockito.when(ingredient1.getType()).thenReturn(type1);
        Mockito.when(ingredient1.getName()).thenReturn("ingredient1");
        Mockito.when(ingredient1.getPrice()).thenReturn(100f);

        ingredient2 = Mockito.mock(Ingredient.class);
        Mockito.when(ingredient2.getType()).thenReturn(type2);
        Mockito.when(ingredient2.getName()).thenReturn("ingredient2");
        Mockito.when(ingredient2.getPrice()).thenReturn(200f);

        burger.setBuns(bun);
    }



    @Test
    public void addIngredientListSizeTesting() {
        burger.addIngredient(ingredient1);
        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void addIngredientCorrectTesting() {
        burger.addIngredient(ingredient1);
        assertSame(ingredient1, burger.ingredients.get(0));
    }




    @Test
    public void setBunsTesting() {
        Bun newBun = Mockito.mock(Bun.class);
        burger.setBuns(newBun);
        assertSame(newBun, burger.bun);
    }


    @Test
    public void removeIngredientListSizeTesting() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.removeIngredient(0);
        assertEquals(1, burger.ingredients.size());
    }

    @Test
    public void removeIngredientCorrectTesting() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.removeIngredient(0);
        assertSame(ingredient2, burger.ingredients.get(0));
    }


    @Test
    public void moveIngredientListSizeTesting() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.moveIngredient(0, 1);
        assertEquals(2, burger.ingredients.size());
    }

    @Test
    public void moveIngredientCorrectTesting() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.moveIngredient(0, 1);
        assertSame(ingredient2, burger.ingredients.get(0));
    }

    @Test
    public void moveIngredientsCorrectTesting() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.moveIngredient(0, 1);
        assertSame(ingredient1, burger.ingredients.get(1));
    }


    @Test
    public void getPriceTesting() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        float expectedPrice = bun.getPrice() * 2 + ingredient1.getPrice() + ingredient2.getPrice();
        assertEquals(expectedPrice, burger.getPrice(), 0.0f);
    }

    @Test
    public void getReceiptBunNameTesting() {
        burger.addIngredient(ingredient1);
        String receipt = burger.getReceipt();
        assertTrue(receipt.contains("(==== white bun ===="));
    }

    @Test
    public void getReceiptCorrectTesting() {
        burger.addIngredient(ingredient1);
        String expectedLine = String.format("= %s %s =",
                type1.toString().toLowerCase(),
                ingredient1.getName());
        assertTrue(burger.getReceipt().contains(expectedLine));
    }

    @Test
    public void getReceiptPriceTesting() {
        burger.addIngredient(ingredient1);
        float expectedPrice = bun.getPrice() * 2 + ingredient1.getPrice();
        assertTrue(burger.getReceipt().contains(String.format("Price: %f", expectedPrice)));
    }

    @Test
    public void GetReceiptWithNoIngredientsTesting() {
        String receipt = burger.getReceipt();
        String expected = String.format(
                "(==== white bun ====)%n" +
                        "(==== white bun ====)%n" +
                        "%n" +
                        "Price: %f%n",
                bun.getPrice() * 2
        );
        assertEquals(expected, receipt);
    }
}



