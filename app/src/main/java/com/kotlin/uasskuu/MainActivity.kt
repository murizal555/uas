package com.ukmtechcode.projekmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainActivity : AppCompatActivity(),BeritaClickListenerRv {
    private lateinit var rvBerita: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvBerita = findViewById(R.id.recycleViewBerita)
        rvBerita.setHasFixedSize(true)

        rvBerita.layoutManager = LinearLayoutManager(this)

        val beritaApi = BeritaHelper.getInstance().create(BeritaApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = beritaApi.getBerita()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val listBeritaAdapter = BeritaListAdapter(response.body()!!)
                        rvBerita.adapter = listBeritaAdapter
                        listBeritaAdapter.clickListener = this@MainActivity
                    } else {
                        Toast.makeText(this@MainActivity, "Error ${response.code()}", Toast.LENGTH_LONG).show()
                    }}
            } catch (e:HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error ${e.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (t: Throwable) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Terjadi kesalahan jaringan", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onItemClickListenter(view: View, berita: Berita) {
        val intentDetail = Intent(this, DetailBerita::class.java)
        intentDetail.putExtra("testing",berita)
        startActivity(intentDetail)
    }
}
