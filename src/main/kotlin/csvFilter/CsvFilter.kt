package csvFilter

class CsvFilter {
    fun filter(lines: List<String>) : List<String> {
        val result = mutableListOf<String>()
        result.add(lines[0])
        val invoice = lines[1]
        val fields = invoice.split(',')
        if (fields[4].isNullOrEmpty() || fields[5].isNullOrEmpty()){
            result.add(lines[1])
        }
        return result.toList()
    }
}

