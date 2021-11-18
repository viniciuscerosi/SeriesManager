import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.edu.ifsp.scl.ads.pdm.seriesmanager.GerenciamentoBD
import br.edu.ifsp.scl.ads.pdm.seriesmanager.model.temporada.Temporada
import br.edu.ifsp.scl.ads.pdm.seriesmanager.model.temporada.TemporadaDAO

class TemporadaSqlite (contexto: Context): TemporadaDAO {
    private val bdSeries: SQLiteDatabase = GerenciamentoBD(contexto).getSeriesBD()

    override fun criarTemporada(temporada: Temporada): Long {
        val temporadaCv = ContentValues()
        temporadaCv.put("numero_sequencial", temporada.numeroSequencialTemp)
        temporadaCv.put("ano_lancamento", temporada.anoLancamentoTemp)
        temporadaCv.put("qtd_episodios", temporada.qtdEpisodiosTemp)
        temporadaCv.put("nome_serie", temporada.nomeSerie)

        return bdSeries.insert("TEMPORADA", null, temporadaCv)
    }

    override fun recuperarTemporadas(nomeSerie: String): MutableList<Temporada> {
        val temporadasList: MutableList<Temporada> = ArrayList()
        val temporadaCursor = bdSeries.rawQuery("SELECT * " +
                "                                    FROM TEMPORADA WHERE nome_serie = ?;", arrayOf(nomeSerie))
        val temporada: Temporada

        if (temporadaCursor.moveToFirst()) {
            while (!temporadaCursor.isAfterLast) {
                val temporada = Temporada(
                    temporadaCursor.getInt(temporadaCursor.getColumnIndexOrThrow("numero_sequencial")),
                    temporadaCursor.getString(temporadaCursor.getColumnIndexOrThrow("ano_lancamento")),
                    temporadaCursor.getString(temporadaCursor.getColumnIndexOrThrow("qtd_episodios")),
                    temporadaCursor.getString(temporadaCursor.getColumnIndexOrThrow("nome_serie"))
                )
                temporadasList.add(temporada)
                temporadaCursor.moveToNext()
            }
        }
        return temporadasList
    }

    override fun removerTemporada(nomeSerie: String, numeroSequencial: Int): Int {
        val numeroSequencialtoString: String = numeroSequencial.toString()
        val temporadaId = buscarTemporadaId(nomeSerie, numeroSequencial)

        //É necessário deletar todos os episódios antes de deletar a temporada
        bdSeries.delete("EPISODIO", "temporada_id = ? ", arrayOf(temporadaId.toString()))

        //Episódios deletados, deletando temporada
        return bdSeries.delete("TEMPORADA", "numero_sequencial = ? AND nome_serie = ?",
            arrayOf(numeroSequencialtoString, nomeSerie)
        )
    }

    override fun buscarTemporadaId(nomeSerie: String, numeroSequencial: Int): Int {
        val temporadaCursor = bdSeries.rawQuery("SELECT id_temporada " +
                "                                   from TEMPORADA WHERE numero_sequencial = ? AND nome_serie = ?",
            arrayOf(numeroSequencial.toString(), nomeSerie))
        var temporadaId: Int = 0

        if (temporadaCursor.moveToFirst()){
            temporadaId = temporadaCursor.getInt(temporadaCursor.getColumnIndexOrThrow("id_temporada"))
        }
        return temporadaId
    }
}