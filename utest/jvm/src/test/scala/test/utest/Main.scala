package test.utest
import utest._
import scala.concurrent.{ExecutionContext, Future}

object Main {
  def main(args: Array[String]): Unit = {
    val tests = Tests{
      'test1{
        //      throw new Exception("test1")
      }
      'test2{
        'inner{
          1
        }
      }
      'test3{
        val a = List[Byte](1, 2)
        //      a(10)
      }
    }

    // Run and return results
    val results1 = TestRunner.run(tests)

    // Run, return results, and print streaming output with the default formatter
    val results2 = TestRunner.runAndPrint(
      tests,
      "MyTestSuite"
    )
    // Run, return results, and print output with custom formatter and executor
    val results3 = TestRunner.runAndPrint(
      tests,
      "MyTestSuite",
      executor = new utest.framework.Executor{
        override def utestWrap(path: Seq[String], runBody: => Future[Any])
                     (implicit ec: ExecutionContext): Future[Any] = {
          println("Getting ready to run " + path.mkString("."))
          runBody
        }
      },
      formatter = new utest.framework.Formatter{
        override def formatColor = false
      }
    )

    object MyTestSuite extends TestSuite{
      val tests = Tests{
        'test1{
          //      throw new Exception("test1")
        }
        'test2{
          'inner{
            1
          }
        }
        'test3{
          val a = List[Byte](1, 2)
          //      a(10)
        }
      }
    }


    // Run `TestSuite` object, and use its configuration for execution and output formatting
    val results4 = TestRunner.runAndPrint(
      MyTestSuite.tests,
      "MyTestSuite",
      executor = MyTestSuite,
      formatter = MyTestSuite
    )
  }
}
