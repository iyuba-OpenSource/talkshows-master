package com.iyuba.wordtest.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iyuba.wordtest.entity.TalkShowWords;

import java.util.List;

;

@Dao
public interface TalkShowWordsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[]  insertWord(TalkShowWords... word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[]  insertWord(List<TalkShowWords> words);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[]  insertWordIgnore(List<TalkShowWords> words);

    @Query("select * from TalkShowWords where book_id = :bookId ")
    List<TalkShowWords> getBookWords(int bookId) ;

    @Query("select * from TalkShowWords where book_id = :bookId and unit_id = :unit order by position asc")
    List<TalkShowWords> getUnitWords(int bookId , int unit ) ;

    @Query("select distinct unit_id from TalkShowWords where book_id = :bookId ")
    List<Integer> getUnitsByBook(int bookId ) ;

    @Query("select * from TalkShowWords where book_id = :bookId and voa_id = :voa ")
    List<TalkShowWords> getUnitByVoa(int bookId , int voa ) ;

    @Query("select distinct voa_id from TalkShowWords where book_id =:bookId and voa_id > 310000 ")
    List<Integer> getVoasByBook(int bookId ) ;

    @Query("select * from TalkShowWords where book_id = :bookId ")
    List<TalkShowWords> getVoasSentencdeAudioByBook(int bookId);

    @Query("select * from TalkShowWords where answer not null ")
    List<TalkShowWords> getAllTestWords();

    @Query("select word from TalkShowWords")
    List<String> getWords();

    @Query("select word from TalkShowWords where book_id = :bookId ")
    List<String> getWords4Book(int bookId);

    @Query("select * from TalkShowWords where book_id = :bookId and unit_id = :unit and position = :position ")
    TalkShowWords getUnitWord(int bookId , int unit , int position) ;

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateSingleWord(TalkShowWords word);

    @Query("select distinct voa_id from TalkShowWords where book_id != :bookId and voa_id > 310000 ")
    List<Integer> getVoasNotIn(int bookId);

    @Query("select distinct videoUrl from TalkShowWords where book_id = :bookId and voa_id > 310000 ")
    List<String> getAudioUrlsByBook(int bookId);

}
