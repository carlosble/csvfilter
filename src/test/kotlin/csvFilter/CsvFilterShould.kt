package csvFilter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
class CsvFilterShould {
    private val headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente"
    lateinit var filter : CsvFilter
    private val emptyFile = listOf(headerLine)
    private val emptyField = ""

    @Before
    fun setup(){
        filter = CsvFilter()
    }

    @Test
    fun correct_lines_are_not_filtered(){
        val invoiceLine = aFileWithLine(concept = "a correct line with irrelevant data")
        val result = filter.apply(invoiceLine)

        assertThat(result).isEqualTo(invoiceLine)
    }

    @Test
    fun tax_fields_are_mutually_exclusive(){
        val result = filter.apply(aFileWithLine(ivaTax = "19", igicTax = "8"))

        assertThat(result).isEqualTo(emptyFile)
    }

    @Test
    fun there_must_be_at_least_one_tax_for_the_invoice(){
        val result = filter.apply(aFileWithLine(ivaTax = emptyField, igicTax = emptyField))

        assertThat(result).isEqualTo(emptyFile)
    }

    @Test
    fun tax_fields_must_be_decimals(){
        val result = filter.apply(aFileWithLine(ivaTax = "XYZ", igicTax = emptyField))

        assertThat(result).isEqualTo(emptyFile)
    }

    @Test
    fun tax_fields_must_be_decimals_and_exclusive(){
        val result = filter.apply(aFileWithLine(ivaTax = "XYZ", igicTax = "12"))

        assertThat(result).isEqualTo(emptyFile)
    }

    private fun aFileWithLine(ivaTax: String = "19", igicTax: String = emptyField, concept: String = "irrelevant"): List<String> {
        val invoiceId = "1"
        val invoiceDate = "02/05/2019"
        val grossAmount = "1000"
        val netAmount = "810"
        val cif = "B76430134"
        val nif = emptyField
        val formattedLine = listOf(
            invoiceId,
            invoiceDate,
            grossAmount,
            netAmount,
            ivaTax,
            igicTax,
            concept,
            cif,
            nif
        ).joinToString(",")
        return listOf(headerLine, formattedLine)
    }
}
