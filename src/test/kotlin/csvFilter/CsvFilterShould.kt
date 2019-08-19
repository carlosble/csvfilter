package csvFilter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
class CsvFilterShould {
    private val headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente, NIF_cliente"

    @Test
    fun correct_lines_are_not_filtered(){
        val invoiceLine = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,"
        val result = CsvFilter().filter(
                listOf(headerLine, invoiceLine))
        assertThat(result).isEqualTo(
                listOf(headerLine, invoiceLine))
    }

    @Test
    fun tax_fields_are_mutually_exclusive(){
        val invoiceLine = "1,02/05/2019,1000,810,19,8,ACER Laptop,B76430134,"
        val result = CsvFilter().filter(
            listOf(headerLine, invoiceLine))
        assertThat(result).isEqualTo(
            listOf(headerLine))
    }

    @Test
    fun there_must_be_at_least_one_tax_for_the_invoice(){
        val invoiceLine = "1,02/05/2019,1000,810,,,ACER Laptop,B76430134,"
        val result = CsvFilter().filter(
            listOf(headerLine, invoiceLine))
        assertThat(result).isEqualTo(
            listOf(headerLine))
    }

    @Test
    fun tax_fields_must_be_decimals(){
        val invoiceLine = "1,02/05/2019,1000,810,XYZ,,ACER Laptop,B76430134,"
        val result = CsvFilter().filter(
            listOf(headerLine, invoiceLine))
        assertThat(result).isEqualTo(
            listOf(headerLine))
    }

    @Test
    fun tax_fields_must_be_decimals_and_exclusive(){
        val invoiceLine = "1,02/05/2019,1000,810,XYZ,12,ACER Laptop,B76430134,"
        val result = CsvFilter().filter(
            listOf(headerLine, invoiceLine))
        assertThat(result).isEqualTo(
            listOf(headerLine))
    }

    /*@Test
    fun ignores_empty_or_null_lists(){
        assertThat(CsvFilter().filter(listOf()))
                .isEqualTo(listOf<String>());
    }*/
}
