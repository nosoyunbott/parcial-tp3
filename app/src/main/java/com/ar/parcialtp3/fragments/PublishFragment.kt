package com.ar.parcialtp3.fragments

import ImageCardAdapter
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ar.parcialtp3.R
import com.ar.parcialtp3.SharedViewModel
import com.ar.parcialtp3.domain.Dog
import com.ar.parcialtp3.domain.Owner
import com.ar.parcialtp3.domain.Provinces
import com.ar.parcialtp3.entities.PublicationEntity
import com.ar.parcialtp3.services.DogDataService
import com.ar.parcialtp3.services.firebase.FirebaseService
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

class PublishFragment : Fragment() {

    lateinit var v: View

    init {
        retainInstance = true
    }


    val MALE = "MALE"
    val FEMALE = "FEMALE"

    //Edit Text
    lateinit var edtAge: EditText
    lateinit var edtName: EditText
    lateinit var edtDescription: EditText
    lateinit var edtWeight: EditText

    lateinit var radioButtonMale: RadioButton
    lateinit var radioButtonFemale: RadioButton
    lateinit var btnPubish: Button
    lateinit var btnAddPhoto: Button
    lateinit var photosList: MutableList<String>

    //Text View
    lateinit var txtPhotoErr: TextView

    //Spinners
    lateinit var spnProvinces: Spinner
    lateinit var provincesAdapter: ArrayAdapter<String>
    var provincesList: List<String> = Provinces().getList()

    lateinit var spnBreeds: Spinner
    lateinit var breedsAdapter: ArrayAdapter<String>
    lateinit var breedsList: List<String>

    lateinit var spnSubBreeds: Spinner
    lateinit var subBreedsAdapter: ArrayAdapter<String>
    var subBreedsList: MutableList<String> = mutableListOf()

    //Misc
    lateinit var selectedProvince: String
    lateinit var selectedBreed: String
    lateinit var selectedSubBreed: String
    lateinit var selectedSex: String
    lateinit var sharedPreferences: SharedPreferences

    lateinit var images: List<String>
    lateinit var selectedImages: List<String>

    var selectedBreedPosition: Int = 0
    var selectedSubBreedPosition: Int = 0
    var selectedProvincePosition: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)


        provincesAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, provincesList)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_publish, container, false)
        //Edit Text initialization
        edtAge = v.findViewById(R.id.edtAge)
        edtName = v.findViewById(R.id.edtName)
        edtDescription = v.findViewById(R.id.edtDescription)
        edtWeight = v.findViewById(R.id.edtWeight)
        radioButtonMale = v.findViewById(R.id.radioButtonMale)
        radioButtonFemale = v.findViewById(R.id.radioButtonFemale)
        btnPubish = v.findViewById(R.id.btnPublish)
        btnAddPhoto = v.findViewById(R.id.btnAddPhoto)
        selectedImages = mutableListOf()


        //Spinners initialization

        sharedPreferences =
            requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)


        spnProvinces = v.findViewById(R.id.spnLocation)
        spnBreeds = v.findViewById(R.id.spnBreed)
        spnSubBreeds = v.findViewById(R.id.spnSubBreed)
        lifecycleScope.launch {
            //val hola = DogDataService().getImagesByBreed("hound")
            val allBreeds = DogDataService().getAllBreeds()

            breedsList = allBreeds.map { it.name.uppercase() }
            //val imagesBySubBreed = DogDataService().getImagesBySubBreed("hound", "afghan")
            // Log.d("imagesBySubBreed", imagesBySubBreed.toString())
            breedsAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, breedsList)

            setUpSpinner(spnBreeds, breedsAdapter)
            setUpSpinner(spnProvinces, provincesAdapter)
            subBreedsAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                subBreedsList
            )
            setUpSpinner(spnSubBreeds, subBreedsAdapter)
            val posSpnBreed = sharedPreferences.getInt("selectedBreed", 0)
            val posSpnSubBreed = sharedPreferences.getInt("selectedSubBreed", 0)
            val posSpnProvince = sharedPreferences.getInt("selectedProvince", 0)
            if (posSpnBreed != 0) {
                spnBreeds.setSelection(posSpnBreed)
                selectedBreed = breedsList[posSpnBreed]
                Log.d("selected breed", selectedBreed)
            }
            if (posSpnSubBreed != 0) {
                spnSubBreeds.setSelection(posSpnSubBreed)
                selectedSubBreed = subBreedsList[posSpnSubBreed]
                Log.d("selected ssssubbreed", selectedBreed)
            }
            if (posSpnProvince != 0) {
                spnProvinces.setSelection(posSpnProvince)
                selectedProvince = provincesList[posSpnProvince]
                Log.d("selected province", selectedBreed)
            }
        }


        return v
    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.selectedImages.observe(viewLifecycleOwner) { selectedImages ->
            this.selectedImages = selectedImages
        }


        photosList = mutableListOf()
        btnAddPhoto.setOnClickListener {
        }

        btnPubish.setOnClickListener {
            val temporaryImageArray = arrayListOf(
                "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg",
                "https://images.dog.ceo/breeds/hound-afghan/n02088094_10263.jpg",
                "https://images.dog.ceo/breeds/hound-afghan/n02088094_10715.jpg"
            )
            handleRadioButtons()
            val name = edtName.text.toString()
            val age = edtAge.text.toString()
            val sex = selectedSex
            val breed = selectedBreed
            val selImages = selectedImages
            val weight = edtWeight.text.toString()
            if (name.isEmpty() || age
                    .isEmpty() || sex.isNullOrEmpty() || breed.isEmpty()
                || selImages.isEmpty() || weight.isEmpty() || selImages.size > 5
            ){
                Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dog = Dog(
                edtName.text.toString(),
                edtAge.text.toString().toInt(),
                selectedSex,
                selectedBreed,
                selectedSubBreed,
                ArrayList(selectedImages),
                false,
                edtWeight.text.toString().toInt()
            )
            val ownerName = sharedPreferences.getString("username", "")
            val ownerPhone = sharedPreferences.getString("phone", "")?.toInt()
            val ownerImage = sharedPreferences.getString("image", "")
            val owner = Owner(ownerName!!, ownerPhone!!, ownerImage!!)
            val publication = PublicationEntity(dog, owner, selectedProvince,edtDescription.text.toString())
            FirebaseService().savePublication(publication)

                val editor = sharedPreferences.edit()
                editor.remove("selectedProvince")
                editor.remove("selectedBreed")
                editor.remove("selectedSubBreed")
                editor.apply()
                sharedViewModel.resetData()
                val action = PublishFragmentDirections.actionPublishFragmentSelf()
                v.findNavController().navigate(action)

        }

    }

    private fun handleRadioButtons() {
        selectedSex = ""
        if(radioButtonFemale.isChecked){
            selectedSex = FEMALE
        }
        if(radioButtonMale.isChecked){
            selectedSex = MALE
        }
        radioButtonFemale.setOnClickListener {
            selectedSex = FEMALE
            radioButtonMale.isChecked = false
        }
        radioButtonMale.setOnClickListener {
            selectedSex = MALE
            radioButtonFemale.isChecked = false
        }
    }

    private fun setUpSpinner(spinner: Spinner, adapter: ArrayAdapter<String>) {
        spinner.adapter = adapter
        selectedSubBreed = ""
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (spinner) {
                    spnProvinces -> {
                        selectedProvince = provincesList[position]
                        selectedProvincePosition = position
                    }

                    spnBreeds -> {
                        selectedBreed = breedsList[position]
                        selectedBreedPosition = position
                    }

                    spnSubBreeds -> {
                        selectedSubBreed = subBreedsList[position]
                        selectedSubBreedPosition = position
                    }
                }




                getSubBreedsOf(selectedBreed)
                getImages(selectedBreed, selectedSubBreed)
                Log.d("subred", selectedSubBreed)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //showDialog()
            }
        }
    }

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private fun getImages(selectedBreed: String, selectedSubBreed: String) {
        images = mutableListOf()
        lifecycleScope.launch {
            if (selectedSubBreed != "") {
                images = DogDataService().getImagesBySubBreed(
                    selectedBreed.lowercase(),
                    selectedSubBreed.lowercase()
                )
                //  Log.d("imagesssss", images.toString())
            } else {
                images = DogDataService().getImagesByBreed(selectedBreed.lowercase())
                // Log.d("Images By Breed", images.toString())
            }
        }.invokeOnCompletion {
            btnAddPhoto.setOnClickListener {
                val selectImages = images
                sharedViewModel.selectedImages.value = selectImages
                Log.d("ss", selectImages.toString())
                val action =
                    PublishFragmentDirections.actionPublishFragmentToPhotoSelectionFragment()
                val editor = sharedPreferences.edit()
                editor.putInt("selectedBreed", selectedBreedPosition)
                editor.putInt("selectedSubBreed", selectedSubBreedPosition)
                editor.putInt("selectedProvince", selectedProvincePosition)
                editor.apply()
                v.findNavController().navigate(action)
            }
        }
    }

    private fun getSubBreedsOf(selectedBreed: String) {
        lifecycleScope.launch {
            val allBreds = DogDataService().getAllBreeds()
            val breed = allBreds.find { it.name.uppercase() == selectedBreed }
            val subBreeds = breed?.subBreeds?.map { it.uppercase() }

            subBreedsList.clear()

            if (subBreeds?.find { it != "[]" } != null) {
                subBreedsList.addAll(subBreeds!!)

            }
            subBreedsAdapter.notifyDataSetChanged()
            Log.d("array", breed?.subBreeds.toString())
        }

    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(context).setTitle("Error").setMessage("ERROR SPINNER")
            .setCancelable(true).create()
        dialog.show()
    }
}