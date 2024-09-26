package data.mapper

internal abstract class UIModelMapper<D, U> {

    abstract fun mapToUI(entity: D): U

    abstract fun mapFromUI(model: U): D

    fun mapToUIList(entities: List<D>): List<U> {
        val models = mutableListOf<U>()
        entities.forEach {
            models.add(mapToUI(it))
        }

        return models
    }

    fun mapFromUIList(uiModels: List<U>): List<D> {
        val domains = mutableListOf<D>()
        uiModels.forEach {
            domains.add(mapFromUI(it))
        }

        return domains
    }
}