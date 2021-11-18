package br.edu.ifsp.scl.ads.pdm.seriesmanager.model.episodio

interface EpisodioDAO {
    fun criarEpisodio(episodio: Episodio): Long
    fun recuperarEpisodios(temporadaId: Int): MutableList<Episodio>
    fun recuperarEpisodio(numeroSequencial: Int, temporadaId: Int): Episodio?
    fun atualizarEpisodio(episodio: Episodio): Int
    fun removerEpisodio(temporadaId: Int, numeroSequencial: Int): Int
}