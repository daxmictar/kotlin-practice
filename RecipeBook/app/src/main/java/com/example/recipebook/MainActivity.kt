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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    val recipes = mutableListOf<Recipe>()
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
fun RecipeListScreen(onRecipeClick: (Int) -> Unit) {
    // RecipeCollection.ComposeRecipes()
}

object Destinations {
    const val RECIPE_LIST_SCREEN = "recipe_list_screen"
    const val RECIPE_DETAIL_SCREEN = "recipe_detail_screen/{recipeId}"
    fun recipeDetailsScreen(recipeId: Int): String { return "recipe_detail_screen/$recipeId" }
}

val recipes = listOf(
    Recipe(
        id=1,
        name = "Fried Egg",
        ingredients = listOf("1 egg", "1 tsp butter"),
        instructions = "Butter pan lightly, cook egg to desired consistency"
    ),
    Recipe(
        id=2,
        name = "Pico de Gallo",
        ingredients = listOf("2 roma tomatoes", "1 onion", "1 jalapeno", "1/2 lime", "1/2 bunch cilantro"),
        instructions = "Finely chop and mix ingredients, juice lime."
    ),
)

@Preview(
    showBackground = true,
)
@Composable
fun RecipeAppPreview() {
    RecipeCollection.recipes.addAll(recipes)

    val recipeNavController = rememberNavController()
    NavHost(
        navController = recipeNavController,
        startDestination = Destinations.RECIPE_LIST_SCREEN
    ) {
        composable(route = Destinations.RECIPE_LIST_SCREEN) {
            RecipeCollection.ComposeRecipes(
                onRecipeClick = {
                    recipeId -> recipeNavController.navigate(Destinations.recipeDetailsScreen(recipeId))
                }
            )

            // Experiment:
            //RecipeListScreen(
            //    onRecipeClick = {
            //        recipeId ->
            //            recipeNavController.navigate(Destinations.RECIPE_DETAIL_SCREEN)
            //    }
            //)
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

            RecipeDetailScreen(
                recipe = RecipeCollection.getById(recipeId),
                onBackClick = {
                    recipeNavController.popBackStack()
                }
            )
        }
    }
}


