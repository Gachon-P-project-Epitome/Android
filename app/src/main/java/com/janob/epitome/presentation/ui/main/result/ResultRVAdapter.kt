package com.janob.epitome.presentation.ui.main.result

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.janob.epitome.R
import com.janob.epitome.data.model.response.ResultResponse
import com.janob.epitome.databinding.ItemResultBinding


interface MyItemClickListener {
    // 노래 상세보기
    fun onItemClick(index: Int)
}
class ResultRVAdapter(private var results: List<ResultResponse>) :
    RecyclerView.Adapter<ResultRVAdapter.ResultViewHolder>() {

    private lateinit var mItemClickListener: MyItemClickListener //아래 받은 것을 내부에서 사용하기 위해 선언

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) { //외부에서의 itemClickListner를 받기 위한 함수
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemResultBinding.inflate(inflater, parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(results[position])

        val result = results[position]

        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(position)

            // 해당 아이템만 갱신
            notifyItemChanged(position)
        }
    }

    fun updateResults(newSongs: List<ResultResponse>) {
        results = newSongs
        notifyDataSetChanged() // 데이터 변경 알리기
    }

    override fun getItemCount(): Int = results.size

    inner class ResultViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultResponse) {

            Log.d("result", result.toString())

            binding.result = result

            // Glide를 사용하여 이미지 로드
            Glide.with(binding.root.context)
                .load(result.albumImageUrl) // URL을 사용하여 이미지 로드
                .error(R.drawable.ic_profile) // 에러 발생 시 표시할 이미지 (선택 사항)
                .into(binding.itemTapeAlbumcoverImgIv) // ImageView에 로드
            var similarity = String.format("%.3f", result.similarity*100).toFloat()

            binding.itemSimilarity.progress = similarity
            binding.itemSimilarity.labelText = "유사도: $similarity%"

            binding.executePendingBindings() // 즉시 데이터 바인딩 업데이트
        }
    }
}
