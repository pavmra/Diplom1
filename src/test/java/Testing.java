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
    public void addIngredientTesting() {
        burger.addIngredient(ingredient1);
        assertEquals(1, burger.ingredients.size());
        assertSame(ingredient1, burger.ingredients.get(0));
    }


    @Test
    public void setBunsTesting() {
        Bun newBun = Mockito.mock(Bun.class);
        burger.setBuns(newBun);
        assertSame(newBun, burger.bun);
    }


    @Test
    public void removeIngredientTesting() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.removeIngredient(0);
        assertEquals(1, burger.ingredients.size());
        assertSame(ingredient2, burger.ingredients.get(0));
    }


    @Test
    public void moveIngredientTesting() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.moveIngredient(0, 1);
        assertEquals(2, burger.ingredients.size());
        assertSame(ingredient2, burger.ingredients.get(0));
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
    public void getReceiptTesting() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        String receipt = burger.getReceipt();
        String[] lines = receipt.split(System.lineSeparator());

        assertEquals("(==== white bun ====)", lines[0].trim());


        String expectedIngredient1Line = String.format("= %s %s =",
                type1.toString().toLowerCase(),
                ingredient1.getName());
        String expectedIngredient2Line = String.format("= %s %s =",
                type2.toString().toLowerCase(),
                ingredient2.getName());

        assertTrue(Arrays.asList(lines).contains(expectedIngredient1Line));
        assertTrue(Arrays.asList(lines).contains(expectedIngredient2Line));

        assertEquals("(==== white bun ====)", lines[lines.length-3].trim());
        assertTrue(lines[lines.length-2].isEmpty()); // Пустая строка
        assertTrue(lines[lines.length-1].startsWith("Price: "));

        float expectedPrice = bun.getPrice() * 2 + ingredient1.getPrice() + ingredient2.getPrice();
        assertTrue(receipt.contains(String.format("Price: %f", expectedPrice)));
    }

    @Test
    public void testGetReceiptWithNoIngredients() {
        String receipt = burger.getReceipt();

        String[] lines = receipt.split(System.lineSeparator());


        assertEquals("(==== white bun ====)", lines[0].trim());
        assertEquals("(==== white bun ====)", lines[1].trim());
        assertTrue(lines[2].isEmpty()); // Пустая строка перед Price
        assertTrue(lines[3].startsWith("Price: "));


        String expectedReceipt = String.format(
                "(==== white bun ====)%n" +
                        "(==== white bun ====)%n" +
                        "%n" +
                        "Price: %f%n",
                bun.getPrice() * 2
        );
        assertEquals(expectedReceipt, receipt);
    }
}



