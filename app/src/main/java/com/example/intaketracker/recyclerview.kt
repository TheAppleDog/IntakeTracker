package com.example.intaketracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class recyclerview : AppCompatActivity(),RecipeAdapter.OnItemClickListener {
    private lateinit var recipeModalArrayList: ArrayList<RecipeModal>
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)

        val courseRV = findViewById<RecyclerView>(R.id.idRVCourse)
        val k = admin_DBHelper(this)
        fun getResourceAsByteArray(context: Context, resourceId: Int): ByteArray? {
            try {
                val inputStream: InputStream = context.resources.openRawResource(resourceId)
                val outputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                inputStream.close()
                return outputStream.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }
        // Here, we have created new array list and added data to it
        recipeModalArrayList= ArrayList<RecipeModal>()
        // Replace this part with your actual database query
        GlobalScope.launch(Dispatchers.IO) {
            val res1 = R.drawable.r1
            val imagebytearray1 = getResourceAsByteArray(applicationContext, res1)
            if (imagebytearray1 != null) {
                k.insertrecipelist(
                    imagebytearray1,
                    "INSTANT POT ROTINI WITH CHICKEN AND BELL PEPPERS",
                    "407 Kcal",
                    "3 cups (710 ml) low-sodium chicken or vegetable broth\n2 tablespoons (30g) no-salt added tomato paste\n4 cups (12 ounces/340g) dry whole-grain rotini pasta\n1/4 cup (27g) oil-packed sun-dried tomatoes, drained and chopped\n1 tablespoon (15 ml) olive oil",
                    "Serves: 4 | Serving Size: 1 1/2 cups pasta mixture\n" +
                            "\n" +
                            "Nutrition (per serving): Calories: 407; Total Fat: 11.2g; Saturated Fat: 2.5g; Monounsaturated Fat: 4.5g; Cholesterol: 78.6mg; Sodium: 707mg; Carbohydrate: 66.6g; Dietary Fiber: 10g; Sugar: 4.1g; Protein: 48.5g\n\nIn the pot of an electric pressure cooker, whisk together the broth and tomato paste until the tomato paste is dissolved. Add the pasta, sun-dried tomatoes, olive oil, rosemary, 1/4 teaspoon salt and several grinds of pepper. Stir to combine. Place the bell peppers on top of the pasta. Season the chicken with the garlic seasoning and place on top of the pasta mixture, do not stir.\n" +
                            "\n" +
                            "Lock on the lid, select the Manual/Pressure Cook function. Adjust to high pressure and set timer for 4 minutes. Make sure the steam valve is in the “sealing” position.\n" +
                            "\n" +
                            "When the cooking time is up, carefully quick-release the pressure.\n" +
                            "\n" +
                            "Unlock the lid and transfer the chicken to a clean cutting board. Slice the meat into bite-size pieces and return them to the pot. Add the spinach and cheese and stir to wilt the spinach. Serve immediately."
                )
            }
            val res2 = R.drawable.r2
            val imagebytearray2 = getResourceAsByteArray(applicationContext,res2)
            if(imagebytearray2 != null){
                k.insertrecipelist(imagebytearray2,"SLOW COOKER OAT PORRIDGE WITH BERRIES","139 Kcal","1 cup (240ml) apple juice\n2 cups (475ml) water\n1 cup (240ml) unsweetened almond milk\n1 teaspoon cinnamon\n1/2 teaspoon allspice\n1/2 cup (88g) steel-cut oats\n1/2 cup (90g) millet\n1 cup (123g) fresh raspberries\n1 cup (144g) fresh blueberries","Serves: 5 | Serving size: 1 cup (240ml) porridge and berries\n" +
                        "\n" +
                        "Nutrition (per serving): Calories: 139; Total Fat: 3g; Saturated Fat: 0g; Monounsaturated Fat: 0g; Cholesterol: 0mg; Sodium: 45mg; Carbohydrate: 28g; Dietary Fiber: 5g; Sugar: 10g; Protein: 4g\n\nIn a slow cooker, combine the apple juice, water, almond milk, cinnamon and allspice and whisk to mix. Add the steel-cut oats and millet and stir, then cover and set on low.\n" +
                        "\n" +
                        "Cook for 7 hours. When the mixture is thick, remove the lid and stir to mix well, then stir in the berries.")
            }
            val res3 = R.drawable.r3
            val imagebytearray3 = getResourceAsByteArray(applicationContext,res3)
            if(imagebytearray3 != null){
                k.insertrecipelist(imagebytearray3,"Crunchy Vegan Oat Chocolate Chip Cookies","114 Kcal","1 3/4 cups (168g) gluten-free rolled oats, divided\n1/4 cup (37g) gluten-free flour blend (such as Bob’s Red Mill 1-to-1 Baking Flour)\n3/4 teaspoon gluten-free baking powder\n1/2 teaspoon ground cinnamon\n1/2 teaspoon salt\n1/2 cup (140g) almond butter\n6 tablespoons (89ml) maple syrup\n3 tablespoons coconut oil\n1 teaspoon gluten-free vanilla extract\n1/2 cup (85g) no-sugar added dark chocolate chips (such as Lily’s)","Serves: 24 | Serving Size: 1 cookie\n" +
                        "\n" +
                        "Nutrition (per serving): Calories: 114; Total Fat: 6g; Saturated Fat: 2g; Monounsaturated Fat: 0g; Cholesterol: 0mg; Sodium: 78mg; Carbohydrate: 14g; Dietary Fiber: 2g; Sugar: 4g; Protein: 3g\n\nPreheat the oven to 375°F (190°C). Line 2 baking sheets with parchment paper and position the oven rack in the upper third of the oven.\n" +
                        "\n" +
                        "Put 1 cup (96g) of the oats in a food processor. Add the gluten-free flour blend, baking powder, cinnamon and salt. Pulse until the oats are finely ground, about 25 1-second pulses. Set aside.\n" +
                        "\n" +
                        "In a large bowl, whisk the almond butter, maple syrup, coconut oil and vanilla together until smooth. Add the contents of the food processor and the remaining unprocessed oats and stir to combine. Stir in the chocolate chips.\n" +
                        "\n" +
                        "Scoop the dough by the tablespoonful and arrange on the baking sheets 2 inches (5 cm) apart. Push down on the top of each cookie with a fork to smash it a little (the cookies will not spread when baked). Bake 1 baking sheet at a time until golden brown and crunchy, about 18 minutes. Cool on a wire rack. Store in an airtight container at room temperature for up to 1 week. Makes 24 cookies.")
            }
            val res4 = R.drawable.r4
            val imagebytearray4 = getResourceAsByteArray(applicationContext,res4)
            if(imagebytearray4 != null){
                k.insertrecipelist(imagebytearray4,"QUICK, MICROWAVE-POACHED EGGS ON AVOCADO TOAST","289 Kcal","1 tablespoon dried shallots or onions\n1 teaspoon dried minced garlic, or 1/2 teaspoon granulated garlic\n1 teaspoon sesame seeds\n1 teaspoon poppy or chia seeds\n1 teaspoon caraway seeds\n1/4 teaspoon sea salt flakes\n1 small (6.5 ounces/182g) avocado, halved lengthwise and pitted\n1/4 lemon, cut into wedges\n2 pieces hearty sprouted whole-grain bread, toasted\n2 large eggs","Serves: 2 | Serving Size: 1 toast \n" +
                        "\n" +
                        "Nutrition (per serving): Calories: 289; Total Fat: 18g; Saturated Fat: 4g; Monounsaturated Fat: 7g; Cholesterol: 185mg; Sodium: 441mg; Carbohydrate: 23g; Dietary Fiber: 8g; Sugar: 1g; Protein: 12g\n\nIn a small bowl, combine the shallots, garlic, sesame seeds, poppy seeds, caraway seeds and sea salt; set aside. Mash the avocado in a small bowl. Squeeze the lemon into the avocado and spread liberally on the toast. Set aside.\n" +
                        "\n" +
                        "Pour 1/2 cup (118ml) water into a microwave-safe coffee mug. Crack 1 egg into the mug, cover with a small plate, and microwave on high for 30 seconds. Lift the plate carefully (to let steam escape) and check the egg. If the white is not completely set, cover and continue to microwave in 10-second intervals until the egg white is opaque. (The time varies with the power of the microwave and may take up to 60 seconds.)\n" +
                        "\n" +
                        "Carefully pour off the water in the mug, using a slotted spoon to keep the egg from falling out. Transfer the egg to one of the slices of avocado toast. Repeat the poaching process with the remaining egg and place it on top of the second piece of toast. Sprinkle the toasts with the seed mixture and serve immediately.")
            }
            val res5 = R.drawable.r5
            val imagebytearray5 = getResourceAsByteArray(applicationContext,res5)
            if(imagebytearray5 != null){
                k.insertrecipelist(imagebytearray5,"CHICKPEA VEGETABLE CURRY","385 Kcal","2 15-ounce (425g) cans of chickpeas\n1 pound frozen (450g) broccoli/cauliflower/carrot blend\n1 bell pepper, diced\n1 15-ounce (425mL) can lite coconut milk\n1 tablespoon curry paste\n1 teaspoon curry powder\n1 cup (200g) brown rice, cooked","Serves: 4  |  Serving Size: 1/4 recipe\n" +
                        "\n" +
                        "Per serving: Calories: 385; Total Fat: 11g; Saturated Fat: 6g; Monounsaturated Fat: 0g; Cholesterol: 0mg; Sodium: 120mg; Carbohydrate: 58g; Dietary Fiber: 12g; Sugar: 7g; Protein: 16g\n\nPlace all ingredients except brown rice in a large pot and simmer until heated through and slightly thickened, about 20 minutes. Serve over brown rice.")
            }
            val res6 = R.drawable.r6
            val imagebytearray6 = getResourceAsByteArray(applicationContext,res6)
            if(imagebytearray6 != null){
                k.insertrecipelist(imagebytearray6,"Oats Uttapam Pancakes","169 Kcal","For batter\n\n1 cup (100g) Quaker Oats\n1/2 cup (50g) garbanzo (gram) flour\n1/4 teaspoon asafoetida (hing) powder\n1/2 teaspoon baking powder\nSalt to taste\n3/4 cup water\n\nFor topping\n\n1/4 cup (40g) grated carrots\n1 onion, chopped\n1 medium tomato, chopped\n1/4 cup (40g) boiled peas\n1/4 cup (40g) chopped cabbage\n1–2 chopped green chilies\n1/4 teaspoon black pepper powder\n2 curry leaves, chopped\nSalt to taste","Serves: 4 | Serving size: 1 pancake\n" +
                        "\n" +
                        "Nutrition (per serving): Calories: 169; Total Fat: 2.2g; Saturated Fat: 0.3g; Monounsaturated Fat: 0.5g; Cholesterol: 0mg; Sodium: 44.4mg; Carbohydrate: 31.9g; Dietary Fiber: 5.4g; Sugar: 3.2g; Protein: 7.7g\n\nMix all batter ingredients until you get a thick pouring consistency. Set batter aside for 30 minutes.\n" +
                        "\n" +
                        "After about 30 minutes, heat a non-stick pan over a low heat and pour one ladle of batter into the pan. Spread the batter into a slightly thick circle in the pan and sprinkle the toppings over the entire batter area. Press the toppings lightly into the batter with a spoon to set.\n" +
                        "\n" +
                        "When one side is crisp and golden brown, flip the batter and brown the other side. Serve hot.")
            }
            val dataFromDb = k.fetchRecipes() // Modify this according to your DBHelper

            // Update the RecyclerView on the main thread
            withContext(Dispatchers.Main) {
                for (dataItem in dataFromDb) {
                    val recipeModal = RecipeModal(
                        dataItem.recipeName,
                        dataItem.calories,
                        dataItem.recipeImage
                    )
                    recipeModalArrayList.add(recipeModal)
                }
                val courseAdapter =
                    RecipeAdapter(this@recyclerview, recipeModalArrayList, this@recyclerview)
                val linearLayoutManager = LinearLayoutManager(
                    this@recyclerview,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                courseRV.layoutManager = linearLayoutManager
                courseRV.adapter = courseAdapter
            }
        }
    }
    override fun onItemClick(position: Int) {
        Log.d("Recycle", "clicked")
        val intent = Intent(this, recipedisplay::class.java)
        // Get the selected recipe from your list
        val selectedRecipe = recipeModalArrayList[position]
        // Add recipe data as extras to the intent
        intent.putExtra("recipeName", selectedRecipe.getRecipe_name())
        intent.putExtra("recipeImage", selectedRecipe.getRecipe_image())
        intent.putExtra("calories", selectedRecipe.getRecipe_calories())

        startActivity(intent)
    }

}