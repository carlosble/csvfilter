package csvFilter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
class CsvFilterShould {
    private val headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente"
    lateinit var filter : CsvFilter
    private val emptyDataFile = listOf(headerLine)
    private val emptyField = ""

    @Before
    fun setup(){
        filter = CsvFilter()
    }

    @Test
    fun correct_lines_are_not_filtered(){
        val lines = fileWithOneInvoiceLineHaving(concept = "a correct line with irrelevant data")
        val result = filter.apply(lines)

        assertThat(result).isEqualTo(lines)
    }

    @Test
    fun tax_fields_are_mutually_exclusive(){
        val result = filter.apply(fileWithOneInvoiceLineHaving(ivaTax = "19", igicTax = "8"))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun there_must_be_at_least_one_tax_for_the_invoice(){
        val result = filter.apply(fileWithOneInvoiceLineHaving(ivaTax = emptyField, igicTax = emptyField))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun tax_fields_must_be_decimals(){
        val result = filter.apply(fileWithOneInvoiceLineHaving(ivaTax = "XYZ", igicTax = emptyField))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    @Test
    fun tax_fields_must_be_decimals_and_exclusive(){
        val result = filter.apply(fileWithOneInvoiceLineHaving(ivaTax = "XYZ", igicTax = "12"))

        assertThat(result).isEqualTo(emptyDataFile)
    }

    private fun fileWithOneInvoiceLineHaving(ivaTax: String = "19", igicTax: String = emptyField, concept: String = "irrelevant"): List<String> {
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
