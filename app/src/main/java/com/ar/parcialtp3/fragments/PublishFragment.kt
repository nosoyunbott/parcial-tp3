package com.ar.parcialtp3.fragments

import android.app.AlertDialog
import android.content.Context
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
import androidx.lifecycle.lifecycleScope
import com.ar.parcialtp3.R
import com.ar.parcialtp3.entities.DogImages
import com.ar.parcialtp3.entities.Provinces
import com.ar.parcialtp3.services.ActivityServiceApiBuilder
import com.ar.parcialtp3.services.DogDataService
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PublishFragment : Fragment() {

    lateinit var v: View

    //Edit Text
    lateinit var edtAge: EditText
    lateinit var edtName: EditText
    lateinit var edtDescription: EditText
    lateinit var edtObservation: EditText
    lateinit var edtWeight: EditText
    lateinit var edtPhotos: EditText
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
    lateinit var subBreedsList: List<String>

    //Misc
    lateinit var selectedProvince: String
    lateinit var selectedBreed: String
    override fun onAttach(context: Context) {
        super.onAttach(context)


        provincesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, provincesList)

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
        edtObservation = v.findViewById(R.id.edtObservation)
        edtWeight = v.findViewById(R.id.edtWeight)
        edtPhotos = v.findViewById(R.id.edtPhotos)
        radioButtonMale = v.findViewById(R.id.radioButtonMale)
        radioButtonFemale = v.findViewById(R.id.radioButtonFemale)
        btnPubish = v.findViewById(R.id.btnPublish)
        btnAddPhoto = v.findViewById(R.id.btnAddPhoto)


        //Spinners initialization



        spnProvinces = v.findViewById(R.id.spnLocation)
        spnBreeds = v.findViewById(R.id.spnBreed)
        spnSubBreeds = v.findViewById(R.id.spnSubBreed)
        lifecycleScope.launch {
            //val hola = DogDataService().getImagesByBreed("hound")
            val allBreeds = DogDataService().getAllBreeds()

            breedsList = allBreeds.map { it.name.uppercase() }
            //val imagesBySubBreed = DogDataService().getImagesBySubBreed("hound", "afghan")
            // Log.d("imagesBySubBreed", imagesBySubBreed.toString())
            breedsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, breedsList)
            setUpSpinner(spnBreeds, breedsAdapter)
            setUpSpinner(spnProvinces, provincesAdapter)
        }


        return v
    }

    override fun onStart() {
        super.onStart()



        photosList = mutableListOf()
        btnAddPhoto.setOnClickListener{
            if(photosList.size < 5){
                photosList.add(edtPhotos.text.toString())
            }else{
                btnAddPhoto.isEnabled=false
                btnAddPhoto.isClickable=false
                Toast.makeText(
                    context,
                    "Has excedido el límite de selección de imágenes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnPubish.setOnClickListener{

        }

    }

    private fun setUpSpinner(spinner: Spinner, adapter: ArrayAdapter<String>) {
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (spinner) {
                    spnProvinces -> selectedProvince = provincesList[position]
                    spnBreeds -> selectedBreed = breedsList[position]
                }
                getSubBreedsOf(selectedBreed)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showDialog()
            }
        }
    }

    private fun getSubBreedsOf(selectedBreed: String) {
        lifecycleScope.launch {
            val allBreds = DogDataService().getAllBreeds()
            val breed = allBreds.find {it.name.uppercase() == selectedBreed}
            val subBreeds = breed?.subBreeds
            if(subBreeds?.find { it != "[]" } != null){
                subBreedsList = subBreeds
                subBreedsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subBreedsList)
                setUpSpinner(spnSubBreeds, subBreedsAdapter)
            }
            Log.d("array", breed?.subBreeds.toString())
        }

    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(context).setTitle("Error").setMessage("ERROR SPINNER")
            .setCancelable(true).create()
        dialog.show()
    }
}