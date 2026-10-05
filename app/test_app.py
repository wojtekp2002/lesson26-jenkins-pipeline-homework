import unittest

from app import build_message, health_check


class AppTest(unittest.TestCase):
    def test_build_message_contains_version_and_environment(self):
        message = build_message("2.1.0", "test")

        self.assertIn("2.1.0", message)
        self.assertIn("test", message)

    def test_health_check_is_ok(self):
        self.assertEqual(health_check()["status"], "ok")


if __name__ == "__main__":
    unittest.main()
