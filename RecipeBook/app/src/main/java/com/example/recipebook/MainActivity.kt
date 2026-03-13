package com.example.recipebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipebook.ui.theme.RecipeBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeBookTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var a  = innerPadding
                }
            }
        }
    }
}

data class Recipe(
    val id: Int,
    val name: String,
    val ingredients: List<String>,
    val instructions: String
) {
    @Composable
    fun ComposeRecipe() {
        Column {

            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ingredients:",
                style = MaterialTheme.typography.headlineSmall
            )
            ingredients.forEach { Text(text = it) }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Instructions:",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(text = instructions)
        }
    }
}

object RecipeCollection {
    var recipeNumber: Int = 1

    val recipes = mutableListOf<Recipe>()

    fun addRecipe(name: String, ingredients: List<String>, instructions: String) {
        recipes.add(Recipe(
            id = recipeNumber++,
            name = name,
            ingredients = ingredients,
            instructions = instructions
        ))
    }

    fun getById(id: Int) : Recipe {
        return recipes.first {
            recipe -> recipe.id == id
        }
    }

    @Composable
    fun ComposeRecipes(onRecipeClick: (Int) -> Unit) {
        Column {
            Text(
                text = "Recipes:",
                style = MaterialTheme.typography.headlineLarge
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                recipes.forEach {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onRecipeClick(it.id) }
                    ) {
                        Text(it.name)
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeDetailScreen(recipe: Recipe, onBackClick: () -> Unit) {
    Column {
        Button(onClick = onBackClick) {
            Text(text = "Back")
        }
        recipe.ComposeRecipe()
    }
}

@Composable
fun RecipeAddScreen(onBackClick: () -> Unit) {
    var recipeName by remember { mutableStateOf("") }
    var recipeIngredients by remember { mutableStateOf("") }
    var recipeInstructions by remember { mutableStateOf("") }
    var modifier = Modifier.fillMaxWidth()

    Column(
        modifier = modifier
    ) {
        Button(onClick = onBackClick) {
            Text(text = "Back")
        }
        OutlinedTextField(
            modifier = modifier,
            value = recipeName,
            onValueChange = { recipeName = it },
            label = { Text(text = "Enter a recipe name") }
        )
        OutlinedTextField(
            modifier = modifier,
            value = recipeIngredients,
            onValueChange = { recipeIngredients = it },
            label = { Text(text = "Enter ingredients, each on a new line.") }
        )
        OutlinedTextField(
            modifier = modifier,
            value = recipeInstructions,
            onValueChange = { recipeInstructions = it },
            label = { Text(text = "Enter instructions") }
        )
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
               RecipeCollection.addRecipe(
                   name = recipeName,
                   ingredients = recipeIngredients.split("\n"),
                   instructions = recipeInstructions
               )

               onBackClick()
            }
        ) {
            Text("Add Recipe")
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun RecipeAddScreenPreview() {
    RecipeAddScreen({})
}

object Destinations {
    const val RECIPE_ADD_SCREEN = "recipe_add_screen"
    const val RECIPE_LIST_SCREEN = "recipe_list_screen"
    const val RECIPE_DETAIL_SCREEN = "recipe_detail_screen/{recipeId}"
    fun recipeDetailsScreen(recipeId: Int): String { return "recipe_detail_screen/$recipeId" }
}

@Preview(
    showBackground = true,
)
@Composable
fun RecipeAppPreview() {
    // default recipes
    RecipeCollection.addRecipe(
        name = "Fried Egg",
        ingredients = listOf("1 egg", "1 tsp butter"),
        instructions = "Butter pan lightly, cook egg to desired consistency"
    )
    RecipeCollection.addRecipe(
        name = "Pico de Gallo",
        ingredients = listOf("2 roma tomatoes", "1 onion", "1 jalapeno", "1/2 lime", "1/2 bunch cilantro"),
        instructions = "Finely chop and mix ingredients, juice lime."
    )

    val recipeNavController = rememberNavController()
    NavHost(
        navController = recipeNavController,
        startDestination = Destinations.RECIPE_LIST_SCREEN
    ) {
        composable(route = Destinations.RECIPE_LIST_SCREEN) {
            Column {
                RecipeCollection.ComposeRecipes(
                    onRecipeClick = { recipeId ->
                        recipeNavController.navigate(Destinations.recipeDetailsScreen(recipeId))
                    }
                )

                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        recipeNavController.navigate(Destinations.RECIPE_ADD_SCREEN)
                    }
                ) {
                    Text("Add New Recipe")
                }
            }
        }

        composable(
            route = Destinations.RECIPE_DETAIL_SCREEN,
            arguments = listOf(
                navArgument(name = "recipeId") {
                    type = NavType.IntType
                }
            )
        ) {
            backStackEntry ->
                val recipeId = backStackEntry.arguments?.getInt("recipeId")
                    ?: return@composable

            Column {
                RecipeDetailScreen(
                    recipe = RecipeCollection.getById(recipeId),
                    onBackClick = {
                        recipeNavController.popBackStack()
                    }
                )

            }
        }

        composable(
            route = Destinations.RECIPE_ADD_SCREEN
        ) {
           RecipeAddScreen(
               onBackClick = {
                   recipeNavController.popBackStack()
               },
           )
        }
    }
}


